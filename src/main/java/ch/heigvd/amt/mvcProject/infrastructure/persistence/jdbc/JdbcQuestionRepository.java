package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
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
                    "INSERT INTO tblQuestion(id, title, description, creationDate, tblUser_id) VALUES(?,?,?,?,?)"
            );

            java.sql.Date creationDate = new java.sql.Date(question.getCreationDate().getTime());

            statement.setString(1, question.getId().asString());
            statement.setString(2, question.getTitle());
            statement.setString(3, question.getDescription());
            statement.setDate(4, creationDate);
            statement.setString(5, question.getAuthorId().asString());

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
    public Optional<Question> findByIdWithAllDetails(QuestionId id) {
        // TODO : gérer les tags toujours

        Optional<Question> optionalQuestion = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT Q.id           as 'question_id'," +
                            "       title," +
                            "       Q.description  as 'question_description'," +
                            "       Q.creationDate as 'question_creationDate'," +
                            "       Q.tblUser_id   as 'question_userId'," +
                            "       A.id           as 'answer_id'," +
                            "       A.description  as 'answer_description'," +
                            "       A.creationDate as 'answer_creationDate'," +
                            "       U.username     as 'answer_username'" +
                            "FROM tblQuestion Q" +
                            "         LEFT JOIN tblAnswer A ON Q.id = A.tblQuestion_id" +
                            "         LEFT JOIN tblUser U on A.tblUser_id = U.id " +
                            "WHERE Q.id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();

            Question foundQuestion = null;

            while (rs.next()) {

                if (foundQuestion == null){
                    DateFormat df = DateFormat.getInstance();
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));


                    foundQuestion = Question.builder()
                            .id(new QuestionId(rs.getString("question_id")))
                            .description(rs.getString("question_description"))
                            .title(rs.getString("title"))
                            .creationDate(new Date(rs.getDate("question_creationDate").getTime()))
                            .authorId(new UserId(rs.getString("question_userId")))
                            .build();
                }

                String username = rs.getString("answer_username");

                if(username != null){
                    foundQuestion.addAnswer(Answer.builder()
                            .creationDate(new Date(rs.getDate("answer_creationDate").getTime()))
                            .description(rs.getString("answer_description"))
                            .id(new AnswerId(rs.getString("answer_id")))
                            .questionId(new QuestionId(rs.getString("question_id")))
                            .username(username)
                            .build());
                }

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
                    .title(rs.getString("title"))
                    .build();

            questions.add(foundQuestion);
        }

        rs.close();

        return questions;
    }


}
