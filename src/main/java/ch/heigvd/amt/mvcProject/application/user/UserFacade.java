package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

/**
 * Link the user and the domain, what we offer to the user to interact with the domain
 * In this class we pass a command (to modify data) of a query (to get data)
 */
public class UserFacade {

    private IUserRepository userRepository;

    public UserFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add a new user in memory
     * @param command Object that contains user information
     */
    public void addNewUser(UserCommand command) {
        User submittedUser = User.builder()
                .email(command.getEmail())
                .username(command.getUsername())
                .password(command.getPassword())
                .build();

        userRepository.save(submittedUser);
    }




}
