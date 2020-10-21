package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.question.QuestionQuery;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.application.user.edit.EditUserCommand;
import ch.heigvd.amt.mvcProject.application.user.edit.EditFailedException;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserFacade {

    private IUserRepository userRepository;

    public UserFacade() {
    }

    public UserFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns all users in function of the query in the parameter
     *
     * @param query Query passed
     *              if query == null -> returns all the users
     *              if query.userId != null -> return the unique user with the id
     * @return all users which match to the query
     */
    public UsersDTO getUsers(UserQuery query) {

        Collection<User> allUsers = new ArrayList<>();

        if (query == null) {
            allUsers = userRepository.findAll();
        } else {
            if (query.userId == null ) {
                allUsers = userRepository.findAll();
            } else {
                Optional<User> userFound = userRepository.findById(query.userId);
                if (userFound.isPresent())
                    allUsers.add(userFound.get());
            }
        }

        List<UsersDTO.UserDTO> allUsersDTO =
                allUsers.stream().map(
                        user -> UsersDTO.UserDTO.builder()
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .id(user.getId())
                                .build()).collect(Collectors.toList());

        return UsersDTO.builder().users(allUsersDTO).build();
    }

    /**
     * Removes the user with the id userId passed in parameter in the repository
     * @param userId the id of the user which will be removed
     */
    public void removeUser(UserId userId) {

    }

    /**
     * Edit the user passed by in parameter
     *
     * @param command The command asked by the user
     * @return CurrentUserDTO the new UserDTO for the session
     * @throws EditFailedException an exception is thrown if the password and the confirmation of password are different
     *                             or a runtime exception is thrown by the method "edit" of repository
     */
    public CurrentUserDTO editUser(EditUserCommand command) throws EditFailedException {

        UserId userId = new UserId(command.getId());

        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            throw new EditFailedException("User with the id " + command.getId() + " not found");
        }

        if (!command.getPassword().equals(command.getConfirmationPassword())) {
            throw new EditFailedException("The password and the confirmation of password are different");
        }

        try {

            // For each field which is possible to be updated, we must check if the command takes in account
            // If it's the case, we'll change the value in the database

            User.UserBuilder builder = User.builder();

            builder.id(userId);

            if (!command.getPassword().isEmpty()) {
                builder.clearTextPassword(command.getPassword());
            } else {
                builder.encryptedPassword(existingUser.getEncryptedPassword());
            }

            if (!command.getEmail().isEmpty()) {
                builder.email(command.getEmail());
            } else {
                builder.email(existingUser.getEmail());
            }

            if (!command.getUsername().isEmpty()) {
                builder.username(command.getUsername());
            } else {
                builder.username(existingUser.getUsername());
            }

            User user = builder.build();

            userRepository.edit(user);

            return CurrentUserDTO.builder()
                    .userId(userId)
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();

        } catch (Exception e) {
            throw new EditFailedException(e.getMessage());
        }
    }
}
