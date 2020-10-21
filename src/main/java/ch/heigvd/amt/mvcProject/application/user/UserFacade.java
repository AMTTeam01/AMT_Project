package ch.heigvd.amt.mvcProject.application.user;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.user.edit.EditUserCommand;
import ch.heigvd.amt.mvcProject.application.user.edit.EditFailedException;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;

public class UserFacade {

    private IUserRepository userRepository;

    public UserFacade() {
    }

    public UserFacade(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Edit the user passed by in parameter
     * @param command The command asked by the user
     * @throws EditFailedException an exception is thrown if the password and the confirmation of password are different
     * or a runtime exception is thrown by the method "edit" of repository
     * @return CurrentUserDTO the new UserDTO for the session
     */
    public CurrentUserDTO editUser(EditUserCommand command) throws EditFailedException {

        UserId userId = new UserId(command.getId());

        User existingUser = userRepository.findById(userId).orElse(null);

        if(existingUser == null){
            throw new EditFailedException("User with the id " + command.getId() + " not found");
        }

        if(!command.getPassword().equals(command.getConfirmationPassword())){
            throw new EditFailedException("The password and the confirmation of password are different");
        }

        try{

            // For each field which is possible to be updated, we must check if the command takes in account
            // If it's the case, we'll change the value in the database

            User.UserBuilder builder = User.builder();

            builder.id(userId);

            if(!command.getPassword().isEmpty()) {
                builder.clearTextPassword(command.getPassword());
            } else {
                builder.encryptedPassword(existingUser.getEncryptedPassword());
            }

            if (!command.getEmail().isEmpty()){
                builder.email(command.getEmail());
            } else {
                builder.email(existingUser.getEmail());
            }

            if (!command.getUsername().isEmpty()){
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

        }catch(Exception e){
            throw new EditFailedException(e.getMessage());
        }
    }
}
