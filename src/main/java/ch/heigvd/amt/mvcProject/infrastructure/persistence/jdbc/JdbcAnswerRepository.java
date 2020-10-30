package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions.NotImplementedException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
                    "SELECT A.id as 'answer_id', " +
                            "       description, " +
                            "       creationDate, " +
                            "       tblQuestion_id, " +
                            "       U.id as 'user_id', " +
                            "       userName " +
                            "FROM tblAnswer A " +
                            "         INNER JOIN tblUser U on A.tblUser_id = U.id " +
                            "WHERE A.tblQuestion_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, questionId.asString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                //reset pointer position
                rs.beforeFirst();

                optionalAnswers = Optional.of(getAnswers(rs));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalAnswers;
    }


    @Override
    public void save(Answer answer) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblAnswer(id, description, creationDate, tblQuestion_id, tblUser_id)" +
                            "values (?, ?, ?, ?,?)"
            );

            Timestamp creationDate = new Timestamp(answer.getCreationDate().getTime());

            statement.setString(1, answer.getId().asString());
            statement.setString(2, answer.getDescription());
            statement.setTimestamp(3, creationDate);
            statement.setString(4, answer.getQuestionId().asString());
            statement.setString(5, answer.getUserId().asString());


            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void edit(Answer newEntity) {
        // TODO : gérer l'édition de la question
        throw new NotImplementedException("edit(Answer newEntity) from " + getClass().getName() + " not implemented");
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
                    "SELECT A.id as 'answer_id', " +
                            "       description, " +
                            "       creationDate, " +
                            "       tblQuestion_id, " +
                            "       U.id as 'user_id', " +
                            "       userName " +
                            "FROM tblAnswer A " +
                            "         INNER JOIN tblUser U on A.tblUser_id = U.id " +
                            "WHERE A.id = ?",
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

                Answer foundAnswer = getAnswer(rs);

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
                    "SELECT A.id as 'answer_id', " +
                            "       description, " +
                            "       creationDate, " +
                            "       tblQuestion_id, " +
                            "       U.id as 'user_id', " +
                            "       userName " +
                            "FROM tblAnswer A " +
                            "         INNER JOIN tblUser U on A.tblUser_id = U.id ",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            answers = getAnswers(statement.executeQuery());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return answers;
    }

    @Override
    public int getVoteValue(UserId userId, AnswerId answerId) {

        int voteValue = 0;

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT positiv FROM tblUser_vote_tblAnswer " +
                            "WHERE tblUser_id = ? AND tblAnswer_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, userId.asString());
            statement.setString(2, answerId.asString());

            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                voteValue = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return voteValue;
    }

    @Override
    public void addVote(UserId userId, AnswerId answerId, int vote) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblUser_vote_tblAnswer (tblUser_id, tblAnswer_id, positiv)" +
                            "VALUES (?, ?, ?)" +
                            "ON DUPLICATE KEY UPDATE positiv = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, userId.asString());
            statement.setString(2, answerId.asString());
            statement.setInt(3, vote);
            statement.setInt(4, vote);

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int getVotes(AnswerId answerId) {

        int totalVotes = 0;

        try {
            // First we get the total number of votes
            PreparedStatement voteStatement = dataSource.getConnection().prepareStatement(
                    "SELECT SUM(positiv) FROM tblUser_vote_tblAnswer " +
                            "WHERE tblAnswer_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            voteStatement.setString(1, answerId.asString());

            ResultSet rs = voteStatement.executeQuery();

            while (rs.next()) {
                totalVotes = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return totalVotes;
    }

    /**
     * Return a list of answer returned by the statement
     * @param rs result set returned by the execution of the statement
     * @return The values returned by the SQL query as a list of answer model
     * @throws SQLException
     */
    private ArrayList<Answer> getAnswers(ResultSet rs) throws SQLException {
        ArrayList<Answer> answers = new ArrayList<>();

        while (rs.next()) {
            Answer foundAnswer = getAnswer(rs);
            answers.add(foundAnswer);
        }

        rs.close();

        return answers;
    }

    /**
     * Return a single answer
     * @param rs the pointer in the DB
     * @return the object pointed by rs as model object
     * @throws SQLException
     */
    private Answer getAnswer(ResultSet rs) throws SQLException {
        return Answer.builder()
                .id(new AnswerId(rs.getString("answer_id")))
                .description(rs.getString("description"))
                .creationDate(new Date(rs.getTimestamp("creationDate").getTime()))
                .questionId(new QuestionId(rs.getString("tblQuestion_id")))
                .username(rs.getString("userName"))
                .userId(new UserId(rs.getString("user_id")))
                .build();
    }
}
