package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.TimeZone;

@ApplicationScoped
@Named("JdbcAnswerRepository")
public class JdbcAnswerRepository implements IAnswerRepository {

    @Resource(lookup = "jdbc/help2000DS")
    DataSource dataSource;

    public JdbcAnswerRepository() {
    }

    public JdbcAnswerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ArrayList<Answer>> findByQuestionId(QuestionId questionId) {
        Optional<ArrayList<Answer>> optionalAnswers = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblAnswer WHERE tblQuestion_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, questionId.asString());

            ResultSet rs = statement.executeQuery();

            optionalAnswers = Optional.of(getAnswers(rs));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalAnswers;
    }

    @Override
    public void save(Answer answer) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblAnswer(id, description, vote ,creationDate, tblQuestion_id ) VALUES(?,?,?,?,?)"
            );

            java.sql.Date creationDate = new Date(answer.getCreationDate().getTime());

            statement.setString(1, answer.getId().asString());
            statement.setString(2, answer.getDescription());
            statement.setInt(3,0);
            statement.setDate(4, creationDate);
            statement.setString(5, answer.getQuestionId().asString());

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void remove(AnswerId id) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "DELETE FROM tblAnswer WHERE id = ?"
            );

            statement.setString(1, id.asString());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Answer> findById(AnswerId id) {

        Optional<Answer> optionalAnswer = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblAnswer WHERE id = ?",
                    // the cursor moves in forward or backward directions.
                    // the modifications done in the database are reflected in the ResultSet.
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    // is updatable. i.e. once you get a ResultSet object you can update its contents.
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                rs.first();

                DateFormat df = DateFormat.getInstance();
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                Answer foundAnswer = Answer.builder()
                        .id(new AnswerId(rs.getString("id")))
                        .description(rs.getString("description"))
                        .creationDate(new Date(rs.getDate("creationDate").getTime()))
                        .questionId(new QuestionId(rs.getString("tblQuestion_id")))
                        .build();

                optionalAnswer = Optional.of(foundAnswer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return optionalAnswer;
    }

    @Override
    public Collection<Answer> findAll() {
        Collection<Answer> answers = new ArrayList<>();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblAnswer",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            answers = getAnswers(statement.executeQuery());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return answers;
    }

    private ArrayList<Answer> getAnswers(ResultSet rs) throws SQLException {
        ArrayList<Answer> answers = new ArrayList<>();

        while (rs.next()) {
            Answer foundAnswer = Answer.builder()
                    .id(new AnswerId(rs.getString("id")))
                    .description(rs.getString("description"))
                    .creationDate(new Date(rs.getDate("creationDate").getTime()))
                    .questionId(new QuestionId(rs.getString("tblQuestion_id")))
                    .build();

            answers.add(foundAnswer);
        }

        rs.close();

        return answers;
    }
}
