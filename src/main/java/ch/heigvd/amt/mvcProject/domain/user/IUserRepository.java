package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.IRepository;

public interface IUserRepository extends IRepository<User, UserId> {

    /**
     * Control if the user exist in the database
     *
     * @param userId the user id
     * @return true if the user is in the DB, false otherwise
     */
    boolean isUserExist(UserId userId);

    boolean isUserExist(String username, String password);
}