package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.comment.CommentId;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                            "       U.userName  " +
                            "FROM tblComment C  " +
                            "         INNER JOIN tblQuestion Q on C.tblQuestion_id = Q.id  " +
                            "         INNER JOIN tblUser U on C.tblUser_id = U.id " +
                            "WHERE Q.id = ?",
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
                            "       U.userName  " +
                            "FROM tblComment C  " +
                            "         INNER JOIN tblAnswer A on C.tblQuestion_id = A.id  " +
                            "         INNER JOIN tblUser U on C.tblUser_id = U.id " +
                            "WHERE A.id = ? ",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, answerId.asString());

            ResultSet rs = statement.executeQuery();

            ArrayList<Comment> comments;

            while(rs.next()){
                Comment comment = Comment.builder()
                        .creationDate(new Date(rs.getTimestamp("comment_creationDate").getTime()))
                        .description(rs.getString("comment_description"))
                        .answerId(answerId)
                        .username(rs.getString("userName"))
                        .id(new CommentId(rs.getString("comment_id")))
                        .build();

                comments.add(comment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return optionalComments;
    }

    @Override
    public void save(Comment entity) {

    }

    @Override
    public void remove(CommentId id) {

    }

    @Override
    public Optional<Comment> findById(CommentId id) {
        return Optional.empty();
    }

    @Override
    public Collection<Comment> findAll() {
        return null;
    }
}
