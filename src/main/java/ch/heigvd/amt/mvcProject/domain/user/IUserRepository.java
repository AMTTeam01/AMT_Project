package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.IRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions.DataCorruptionException;

import java.util.Optional;

public interface IUserRepository extends IRepository<User, UserId> {

    /**
     * Find the user which has the username given
     * @param username username of the user
     * @return the "optional" user
     */
    Optional<User> findByUsername(String username);
}