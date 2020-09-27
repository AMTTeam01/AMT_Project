package ch.heigvd.amt.mvcProject.application.authentication;

import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.User;

public class AuthenticationFacade {
    private IUserRepository userRepository;

    public AuthenticationFacade(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void register(RegisterCommand cmd) throws RegistrationFailedException {
        User existingUser = userRepository.findByUsername(cmd.getUsername())
                .orElse(null);

        if(existingUser != null){
            throw new RegistrationFailedException("Username is already used");
        }

        if(!cmd.getClearTxtPassword().equals(cmd.getConfirmationClearTxtPassword())){
            throw new RegistrationFailedException("The password and the confirmation of the password aren't the same");
        }

        try{
            User newUser = User.builder()
                .username(cmd.getUsername())
                .email(cmd.getEmail())
                .clearTextPassword(cmd.getClearTxtPassword())
                .build();

            userRepository.save(newUser);
        }catch(Exception e){
            throw new RegistrationFailedException(e.getMessage());
        }
    }

    public CurrentUserDTO login(LoginCommand cmd) throws LoginFailedException {
        User user = userRepository.findByUsername(cmd.getUsername())
                .orElseThrow(() -> new LoginFailedException("The user hasn't been found"));

        boolean success = user.authenticate(cmd.getClearTxtPassword());

        if(!success)
            throw new LoginFailedException("Check of credentials failed");

        CurrentUserDTO currentUser = CurrentUserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return currentUser;
    }
}
