package ch.heigvd.amt.mvcProject.application.authentication;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationFacadeTest {

    /*private static ServiceRegistry serviceRegistry;
    private static AuthenticationFacade authenticationFacade;

    @BeforeAll
    public static void init(){
        authenticationFacade = serviceRegistry.getAuthenticationFacade();
    }

    @Test
    @Order(1)
    public void itShouldRegisterANewUser() throws RegistrationFailedException, LoginFailedException {
        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("henri")
                .email("henri@gmail.com")
                .build();

        authenticationFacade.register(registerCommand);

        LoginCommand command = LoginCommand
                .builder()
                .clearTxtPassword("1234")
                .username("henri")
                .build();

        CurrentUserDTO currentUserDTO = authenticationFacade.login(command);
        CurrentUserDTO expected = CurrentUserDTO.builder()
                .username("henri")
                .email("henri@gmail.com").build();

        assertEquals(currentUserDTO, expected);
    }

    @Test
    @Order(2)
    public void itShouldntRegisterAUserIfPresent() throws RegistrationFailedException {
        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("patrick")
                .email("henri@gmail.com")
                .build();

        authenticationFacade.register(registerCommand);

        assertThrows(RegistrationFailedException.class, () -> {
            authenticationFacade.register(registerCommand);
        });
    }

    @Test
    @Order(3)
    public void itShouldLoginAUserIfPresent() throws LoginFailedException {
        LoginCommand loginCommand = LoginCommand
                .builder()
                .username("henri")
                .clearTxtPassword("1234")
                .build();

        authenticationFacade.login(loginCommand);
    }

    @Test
    @Order(4)
    public void itShouldThrowIfUserIsntPresent(){
        LoginCommand loginCommand = LoginCommand
                .builder()
                .username("unknown")
                .clearTxtPassword("1234")
                .build();

        assertThrows(LoginFailedException.class, () -> {
            authenticationFacade.login(loginCommand);
        });
    }

    @Test
    @Order(5)
    public void itShouldThrowIfUserIsPresentButPasswordIncorrect() {
        LoginCommand loginCommand = LoginCommand
                .builder()
                .username("henri")
                .clearTxtPassword("unknown")
                .build();

        assertThrows(LoginFailedException.class, () -> {
            authenticationFacade.login(loginCommand);
        });
    }

    @Test
    @Order(6)
    public void itShouldThrowIfPasswordAndConfirmationPasswordArentTheSame(){
        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("12346")
                .username("patrick")
                .email("henri@gmail.com")
                .build();

        assertThrows(RegistrationFailedException.class, () -> {
            authenticationFacade.register(registerCommand);
        });
    }*/

}
