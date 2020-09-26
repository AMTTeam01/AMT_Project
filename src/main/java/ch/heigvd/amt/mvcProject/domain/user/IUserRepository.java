package ch.heigvd.amt.mvcProject.domain.user;

import ch.heigvd.amt.mvcProject.domain.IRepository;

public interface IUserRepository extends IRepository<User, UserId> {

    boolean isUserExist(UserId userId, String password);
}