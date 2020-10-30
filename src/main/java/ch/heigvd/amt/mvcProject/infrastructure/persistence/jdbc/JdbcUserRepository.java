package ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc;

import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Named("JdbcUserRepository")
public class JdbcUserRepository implements IUserRepository {

    @Resource(lookup = "jdbc/help2000DS")
    DataSource dataSource;

    public JdbcUserRepository() {
    }

    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        Optional<User> user = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            if (rs.next())
                user = Optional.of(getUsers(statement.executeQuery()).get(0));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {

        Optional<User> user = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser WHERE userName = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();

            if (rs.next())
                user = Optional.of(getUsers(statement.executeQuery()).get(0));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setString(1, email);

            ResultSet rs = statement.executeQuery();

            if (rs.next())
                user = Optional.of(getUsers(statement.executeQuery()).get(0));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public void save(User entity) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "INSERT INTO tblUser (id, userName, email, encryptedPassword) " +
                            "VALUES (?, ?, ?, ?);"
            );

            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getUsername());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getEncryptedPassword());

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void edit(User newEntity) {
        try{
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "UPDATE tblUser SET userName = ?, email = ?, encryptedPassword = ? " +
                         "WHERE id = ?;"
            );

            statement.setString(1, newEntity.getUsername());
            statement.setString(2, newEntity.getEmail());
            statement.setString(3, newEntity.getEncryptedPassword());
            statement.setString(4, newEntity.getId().asString());

            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void remove(UserId id) {
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "DELETE FROM tblUser WHERE id = ?;"
            );
            statement.setString(1, id.asString());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<User> findById(UserId id) {

        Optional<User> user = Optional.empty();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            statement.setString(1, id.asString());

            ResultSet rs = statement.executeQuery();

            if (rs.next())
                user = Optional.of(getUsers(statement.executeQuery()).get(0));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();

        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    "SELECT * FROM tblUser",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            users = getUsers(statement.executeQuery());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    /**
     * Get all users corresponding to the given result set
     *
     * @param rs : result set
     * @return list of users
     * @throws SQLException
     */
    private ArrayList<User> getUsers(ResultSet rs) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (rs.next()) {

            User foundUser = User.builder()
                    .id(new UserId(rs.getString("id") + ""))
                    .email(rs.getString("email"))
                    .username(rs.getString("userName"))
                    .encryptedPassword(rs.getString("encryptedPassword"))
                    .build();

            users.add(foundUser);
        }

        return users;
    }
}
