package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
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
    public void save(Question entity) {
        // TODO
    }

    @Override
    public void remove(QuestionId id) {
        // TODO
    }

    @Override
    public Optional<Question> findById(QuestionId id) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Collection<Question> findAll() {
        // TODO
        return null;
    }
}
