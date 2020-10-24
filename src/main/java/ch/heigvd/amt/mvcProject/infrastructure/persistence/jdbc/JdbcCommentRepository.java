package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.comment.CommentId;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@ApplicationScoped
@Named("JdbcCommentRepository")
public class JdbcCommentRepository implements ICommentRepository {

    @Resource(lookup = "jdbc/help2000DS")
    DataSource dataSource;

    public JdbcCommentRepository() {
    }

    public JdbcCommentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ArrayList<Comment>> findByQuestionId(QuestionId questionId) {

        Optional<ArrayList<Comment>> optionalComments = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT C.id           AS 'comment_id',  " +
                            "       C.description  AS 'comment_description',  " +
                            "       C.creationDate AS 'comment_creationDate',  " +
                            "       U.userName  ," +
                            "       U.id as 'user_id'  " +
                            "FROM tblComment C  " +
                            "         INNER JOIN tblQuestion Q on C.tblQuestion_id = Q.id  " +
                            "         INNER JOIN tblUser U on C.tblUser_id = U.id " +
                            "WHERE Q.id = ?" +
                            "ORDER BY C.creationDate ASC ",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, questionId.asString());

            ResultSet rs = statement.executeQuery();

            ArrayList<Comment> comments = new ArrayList<>();

            while (rs.next()) {
                Comment comment = Comment.builder()
                        .creationDate(new Date(rs.getTimestamp("comment_creationDate").getTime()))
                        .description(rs.getString("comment_description"))
                        .questionId(questionId)
                        .username(rs.getString("userName"))
                        .userId(new UserId(rs.getString("user_id")))
                        .id(new CommentId(rs.getString("comment_id")))
                        .build();

                comments.add(comment);
            }

            rs.close();

            optionalComments = Optional.of(comments);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return optionalComments;
    }

    @Override
    public Optional<ArrayList<Comment>> findByAnswerId(AnswerId answerId) {

        Optional<ArrayList<Comment>> optionalComments = Optional.empty();


        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT C.id           AS 'comment_id',  " +
                            "       C.description  AS 'comment_description',  " +
                            "       C.creationDate AS 'comment_creationDate',  " +
                            "       U.userName,  " +
                            "       U.id AS 'user_id' " +
                            "FROM tblComment C  " +
                            "         INNER JOIN tblAnswer A on C.tblAnswer_id = A.id  " +
                            "         INNER JOIN tblUser U on C.tblUser_id = U.id " +
                            "WHERE A.id = ? " +
                            "ORDER BY C.creationDate ASC",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, answerId.asString());

            ResultSet rs = statement.executeQuery();

            ArrayList<Comment> comments = new ArrayList<>();


            while (rs.next()) {
                Comment comment = Comment.builder()
                        .creationDate(new Date(rs.getTimestamp("comment_creationDate").getTime()))
                        .description(rs.getString("comment_description"))
                        .answerId(answerId)
                        .username(rs.getString("userName"))
                        .userId(new UserId(rs.getString("user_id")))
                        .id(new CommentId(rs.getString("comment_id")))
                        .build();

                comments.add(comment);
            }

            optionalComments = Optional.of(comments);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalComments;
    }

    @Override
    public void save(Comment comment) {

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblComment(id, description, creationDate, tblAnswer_id, tblQuestion_id, tblUser_id)" +
                            "VALUES (?,?,?,?,?,?)"
            );

            Timestamp creationDate = new Timestamp(comment.getCreationDate().getTime());

            statement.setString(1, comment.getId().asString());
            statement.setString(2, comment.getDescription());
            statement.setTimestamp(3, creationDate);
            statement.setString(4, comment.getAnswerId() == null ? null : comment.getAnswerId().asString());
            statement.setString(5, comment.getQuestionId() == null ? null : comment.getQuestionId().asString());
            statement.setString(6, comment.getUserId().asString());

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void edit(Comment newEntity) {
        // TODO : gérer l'édition de la question
        throw new NotImplementedException("edit(Comment newEntity) from " + getClass().getName() + " not implemented");
    }


    @Override
    public void remove(CommentId id) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "DELETE FROM tblComment WHERE id = ?"
            );

            statement.setString(1, id.asString());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<Comment> findById(CommentId id) {
        Optional<Comment> optionalComment = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT C.id as 'comment_id', " +
                            "       C.creationDate, " +
                            "       C.description, " +
                            "       U.id as 'user_id', " +
                            "       U.userName, " +
                            "       Q.id as 'question_id', " +
                            "       A.id as 'answer_id' " +
                            "from tblComment C " +
                            "         INNER JOIN tblUser U on C.tblUser_id = U.id " +
                            "         LEFT JOIN tblQuestion Q on C.tblQuestion_id = Q.id " +
                            "         LEFT JOIN tblAnswer A on C.tblAnswer_id = A.id " +
                            "WHERE C.id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();


            if (rs.next()) {
                rs.first();

                optionalComment = Optional.of(getCommentAsModel(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return optionalComment;
    }

    @Override
    public Collection<Comment> findAll() {
        Collection<Comment> comments = new ArrayList<>();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT C.id as 'comment_id', " +
                            "       C.creationDate, " +
                            "       C.description, " +
                            "       U.id as 'user_id', " +
                            "       U.userName, " +
                            "       Q.id as 'question_id', " +
                            "       A.id as 'answer_id' " +
                            "from tblComment C " +
                            "         INNER JOIN tblUser U on C.tblUser_id = U.id " +
                            "         LEFT JOIN tblQuestion Q on C.tblQuestion_id = Q.id " +
                            "         LEFT JOIN tblAnswer A on C.tblAnswer_id = A.id ",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                comments.add(getCommentAsModel(rs));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return comments;
    }

    private Comment getCommentAsModel(ResultSet rs) throws SQLException {
        Comment.CommentBuilder commentBuilder = Comment.builder()
                .userId(new UserId(rs.getString("user_id")))
                .id(new CommentId(rs.getString("comment_id")))
                .username(rs.getString("userName"))
                .description(rs.getString("description"))
                .creationDate(new Date(rs.getTimestamp("creationDate").getTime()));

        if (rs.getString("question_id") != null) {
            commentBuilder.questionId(new QuestionId(rs.getString("question_id")));
        }

        if (rs.getString("answer_id") != null) {
            commentBuilder.answerId(new AnswerId(rs.getString("answer_id")));

        }
        return commentBuilder.build();
    }
}
