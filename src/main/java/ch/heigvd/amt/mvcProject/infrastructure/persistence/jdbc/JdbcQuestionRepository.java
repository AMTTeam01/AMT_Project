package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;

@ApplicationScoped
@Named("JdbcQuestionRepository")
public class JdbcQuestionRepository implements IQuestionRepository {

    @Resource(lookup = "jdbc/help2000DS")
    DataSource dataSource;

    public JdbcQuestionRepository() {
    }

    public JdbcQuestionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void save(Question question) {

        // TODO : gérer l'ajout des tags

        try{
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblQuestion(id, title, description, vote, creationDate, tblUser_id) VALUES(?,?,?,?,?,?)"
            );

            java.sql.Date creationDate = new java.sql.Date(question.getCreationDate().getTime());

            statement.setString(1, question.getId().asString());
            statement.setString(2, question.getTitle());
            statement.setString(3, question.getDescription());
            statement.setInt(4, question.getVote());
            statement.setDate(5, creationDate);
            statement.setString(6, question.getAuthorId().asString());

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void remove(QuestionId id) {
        try{
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "DELETE FROM tblQuestion WHERE id = ?"
            );

            statement.setString(1, id.asString());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Question> findById(QuestionId id) {

        // TODO : gérer les tags toujours

        Optional<Question> optionalQuestion = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblQuestion WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();

            if(rs.next()){

                rs.first();

                DateFormat df = DateFormat.getInstance();
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                Question foundQuestion = Question.builder()
                        .id(new QuestionId(rs.getString("id")))
                        .description(rs.getString("description"))
                        .title(rs.getString("title"))
                        .vote(rs.getInt("vote"))
                        .creationDate(new Date(rs.getDate("creationDate").getTime()))
                        .authorId(new UserId(rs.getString("tblUser_id")))
                        .build();

                optionalQuestion = Optional.of(foundQuestion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalQuestion;
    }

    @Override
    public Collection<Question> findAll() {
        Collection<Question> questions = new ArrayList<>();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblQuestion",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            questions = getQuestions(statement.executeQuery());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return questions;
    }

    @Override
    public void addVote(int voteValue, QuestionId id) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "UPDATE tblQuestion SET vote = ? WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            statement.setInt(1, voteValue);
            statement.setString(2, id.asString());

            statement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get all users corresponding to the given result set
     * @param rs : result set
     * @return list of users
     * @throws SQLException
     */
    private ArrayList<Question> getQuestions(ResultSet rs) throws SQLException {
        ArrayList<Question> questions = new ArrayList<>();

        while(rs.next()) {

            Question foundQuestion = Question.builder()
                    .id(new QuestionId(rs.getString("id")))
                    .creationDate(new Date(rs.getDate("creationDate").getTime()))
                    .authorId(new UserId(rs.getString("tblUser_id")))
                    .description(rs.getString("description"))
                    .vote(rs.getInt("vote"))
                    .title(rs.getString("title"))
                    .build();

            questions.add(foundQuestion);
        }

        rs.close();

        return questions;
    }
}
