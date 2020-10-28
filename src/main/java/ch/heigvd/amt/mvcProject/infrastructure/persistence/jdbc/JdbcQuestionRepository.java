package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

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
        System.out.println("SAVING : " + question.getId().asString());
        // TODO : gerer l'ajout des tags

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblQuestion(id, title, description, creationDate, tblUser_id)" +
                            "VALUES (?, ?, ?, ?, ?)"
            );

            Timestamp creationDate = new Timestamp(question.getCreationDate().getTime());

            statement.setString(1, question.getId().asString());
            statement.setString(2, question.getTitle());
            statement.setString(3, question.getDescription());
            statement.setTimestamp(4, creationDate);
            statement.setString(5, question.getUserId().asString());

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void edit(Question newEntity) {
        // TODO : gerer l'édition de la question
        throw new NotImplementedException("edit(Question newEntity) from " + getClass().getName() + " not implemented");
    }

    @Override
    public void remove(QuestionId id) {
        System.out.println("REMOVING QUESTION : " + id.asString());
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "DELETE FROM tblQuestion WHERE id = ?"
            );

            // Removing all votes related to the question
            // rem: could be done with ON CASCADE but didn't work with jdbc...
            PreparedStatement votesStatement = dataSource.getConnection().prepareStatement(
                    "DELETE FROM tblUser_vote_tblQuestion WHERE tblQuestion_id = ?"
            );

            statement.setString(1, id.asString());
            votesStatement.setString(1, id.asString());

            statement.execute();
            votesStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Question> findById(QuestionId id) {

        // TODO : gerer les tags toujours

        Optional<Question> optionalQuestion = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT Q.id as 'question_id'," +
                            "       Q.creationDate," +
                            "       Q.description," +
                            "       Q.title," +
                            "       U.id AS 'user_id'," +
                            "       U.userName " +
                            "       FROM tblQuestion Q " +
                            "JOIN tblUser U on Q.tblUser_id = U.id " +
                            "WHERE Q.id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                rs.first();

                Question foundQuestion = getQuestion(rs);

                optionalQuestion = Optional.of(foundQuestion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalQuestion;
    }

    @Override
    public Optional<Question> findByIdWithAllDetails(QuestionId id) {
        // TODO : gérer les tags toujours

        Optional<Question> optionalQuestion = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT Q.id           as 'question_id', " +
                            "       title, " +
                            "       Q.description  as 'question_description', " +
                            "       Q.creationDate as 'question_creationDate', " +
                            "       UQ.userName    as 'question_username', " +
                            "       UQ.id          as 'question_user_id', "+
                            "       A.id           as 'answer_id', " +
                            "       A.description  as 'answer_description', " +
                            "       A.creationDate as 'answer_creationDate', " +
                            "       UA.id          as 'answer_user_id' " +
                            "FROM tblQuestion Q " +
                            "         LEFT JOIN tblAnswer A ON Q.id = A.tblQuestion_id " +
                            "         LEFT JOIN tblUser UA on A.tblUser_id = UA.id " +
                            "         LEFT JOIN tblUser UQ ON Q.tblUser_id = UQ.id " +
                            "WHERE Q.id = ? " +
                            "ORDER BY A.creationDate ASC",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();

            Question foundQuestion = null;

            while (rs.next()) {

                if (foundQuestion == null) {
                    foundQuestion = Question.builder()
                            .id(new QuestionId(rs.getString("question_id")))
                            .description(rs.getString("question_description"))
                            .title(rs.getString("title"))
                            .creationDate(new Date(rs.getTimestamp("question_creationDate").getTime()))
                            .userId(new UserId(rs.getString("question_user_id")))
                            .build();
                }

                /*String username = rs.getString("answer_username");*/

                foundQuestion.addAnswer(Answer.builder()
                        .id(new AnswerId(rs.getString("answer_id")))
                        .creationDate(new Date(rs.getTimestamp("answer_creationDate").getTime()))
                        .description(rs.getString("answer_description"))
                        .questionId(new QuestionId(rs.getString("question_id")))
                        .userId(new UserId(rs.getString("answer_user_id")))
                        .build());
            }
            optionalQuestion = Optional.of(foundQuestion);

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
                    "SELECT Q.id as 'question_id'," +
                            "       Q.creationDate," +
                            "       Q.description," +
                            "       Q.title," +
                            "       U.id as 'user_id' " +
                            "       FROM tblQuestion Q " +
                            "INNER JOIN tblUser U on Q.tblUser_id = U.id",
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
    public int getVotes(QuestionId questionId) {

        int totalVotes = 0;

        try {
            // First we get the total number of votes
            PreparedStatement voteStatement = dataSource.getConnection().prepareStatement(
                    "SELECT SUM(positiv) FROM tblUser_vote_tblQuestion " +
                            "WHERE tblQuestion_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            voteStatement.setString(1, questionId.asString());

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
     * Adds a vote to a question
     * @param userId     : user that voted
     * @param questionId : question to add the vote to
     * @param positive   : vote value (-1, 0 or 1)
     */
    @Override
    public void addVote(UserId userId, QuestionId questionId, int positive) {
        System.out.println("VOTING FOR " + questionId.asString() + " - " + positive);
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblUser_vote_tblQuestion (tblUser_id, tblQuestion_id, positiv)" +
                            "VALUES (?, ?, ?)" +
                            "ON DUPLICATE KEY UPDATE positiv = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, userId.asString());
            statement.setString(2, questionId.asString());
            statement.setInt(3, positive);
            statement.setInt(4, positive);

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean hasAlreadyVoted(UserId userId, QuestionId questionId) {

        boolean result = false;

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser_vote_tblQuestion " +
                            "WHERE tblUser_id = ? AND tblQuestion_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, userId.asString());
            statement.setString(2, questionId.asString());

            ResultSet rs = statement.executeQuery();
            result = rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public int getVoteValue(UserId userId, QuestionId questionId) {

        int voteValue = 0;

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser_vote_tblQuestion " +
                            "WHERE tblUser_id = ? AND tblQuestion_id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, userId.asString());
            statement.setString(2, questionId.asString());

            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                voteValue = rs.getInt(3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return voteValue;
    }

    /**
     * Get all questions corresponding to the given result set
     *
     * @param rs : result set
     * @return list of questions
     * @throws SQLException
     */
    private ArrayList<Question> getQuestions(ResultSet rs) throws SQLException {
        ArrayList<Question> questions = new ArrayList<>();

        while (rs.next()) {

            Question foundQuestion = getQuestion(rs);

            questions.add(foundQuestion);
        }

        rs.close();

        return questions;
    }

    /**
     * Return a single question pointed by rs
     * @param rs result set
     * @return the question pointed by rs
     * @throws SQLException
     */
    private Question getQuestion(ResultSet rs) throws SQLException {
        return Question.builder()
                .id(new QuestionId(rs.getString("question_id")))
                .creationDate(new Date(rs.getTimestamp("creationDate").getTime()))
                .userId(new UserId(rs.getString("user_id")))
                .description(rs.getString("description"))
                .title(rs.getString("title"))
                .build();
    }
}
