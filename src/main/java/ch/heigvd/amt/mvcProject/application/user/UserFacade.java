package ch.heigvd.amt.mvcProject.application.user;


import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;

public class UserFacade {
    private IUserRepository userRepository;

    public UserFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
