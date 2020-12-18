package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.user.edit.EditUserCommand;
import ch.heigvd.amt.mvcProject.application.user.edit.EditFailedException;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

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
     * @return Returns all users
     */
    public UsersDTO getUsers() {
        Collection<User> allUsers = userRepository.findAll();

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
     * Returns all users in function of the query in the parameter
     *
     * @param query Query passed
     *              if query.userId != null -> return the unique user with the id
     * @return all users which match to the query
     */
    public UsersDTO getUsers(UserQuery query) throws UserFailedException {

        Collection<User> allUsers = new ArrayList<>();

        if (query == null) {
            throw new UserFailedException("query is null");
        } else {
            if (query.userId == null) {
                throw new UserFailedException("query.userId is null");
            } else {
                Optional<User> userFound = userRepository.findById(query.userId);
                userFound.ifPresent(allUsers::add);
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
     *
     * @param userId the id of the user which will be removed
     */
    public void removeUser(UserId userId) {
        userRepository.remove(userId);
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

        UserId userId = null;
        if (command.getId() != null)
            userId = new UserId(command.getId());
        else
            throw new EditFailedException("command.id is null");

        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser == null) {
            throw new EditFailedException("User with the id " + command.getId() + " not found");
        }

        if (!command.getPassword().equals(command.getConfirmationPassword())) {
            throw new EditFailedException("The password and the confirmation of password are different");
        }

        if (userRepository.findByUsername(command.getUsername()).isPresent()) {
            throw new EditFailedException("The username already exist");
        }

        if(userRepository.findByEmail(command.getEmail()).isPresent()){
            throw new EditFailedException("The email already exist");
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
