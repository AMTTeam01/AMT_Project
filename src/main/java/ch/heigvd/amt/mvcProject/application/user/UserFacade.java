package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;

public class UserFacade {

    private IUserRepository userRepository;

    public UserFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewUser(NewUserCommand command){
        User submittedUser = User.builder()
                .email(command.getEmail())
                .username(command.getUsername())
                .password(command.getPassword())
                .build();

        userRepository.save(submittedUser);
    }

}
