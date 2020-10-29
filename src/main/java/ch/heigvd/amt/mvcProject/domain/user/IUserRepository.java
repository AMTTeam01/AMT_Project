package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions.DataCorruptionException;

import java.util.Optional;


public interface IUserRepository extends IRepository<User, UserId> {

    /**
     * @param username username of the user
     * @return the user stored
     */
    Optional<User> findByUsername(String username);

    /**
     * an e-mail should be unique, so there exists only one user in this world with the e-mail
     * @param email e-mail of the user
     * @return the user stored
     */
    Optional<User> findByEmail(String email);
}