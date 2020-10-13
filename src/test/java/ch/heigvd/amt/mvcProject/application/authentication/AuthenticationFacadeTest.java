package ch.heigvd.amt.mvcProject.application.authentication;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import org.junit.jupiter.api.*;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import static org.junit.jupiter.api.Assertions.*;

/*
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationFacadeTest {

    static SeContainerInitializer initializer;

    @BeforeAll
    public static void init(){
        initializer = SeContainerInitializer.newInstance();
    }

    @Test
    @Order(1)
    public void itShouldRegisterANewUser() throws RegistrationFailedException, LoginFailedException {
        try (SeContainer container = initializer.initialize()) {
            ServiceRegistry serviceRegistry = container.select(ServiceRegistry.class).get();
            AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

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
    }

    @Test
    @Order(2)
    public void itShouldntRegisterAUserIfPresent() throws RegistrationFailedException {
        try (SeContainer container = initializer.initialize()) {
            AuthenticationFacade authenticationFacade = container.select(AuthenticationFacade.class).get();

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
    }

    @Test
    @Order(3)
    public void itShouldLoginAUserIfPresent() throws LoginFailedException {
        try (SeContainer container = initializer.initialize()) {
            AuthenticationFacade authenticationFacade = container.select(AuthenticationFacade.class).get();
            LoginCommand loginCommand = LoginCommand
                    .builder()
                    .username("henri")
                    .clearTxtPassword("1234")
                    .build();

            authenticationFacade.login(loginCommand);
        }
    }

    @Test
    @Order(4)
    public void itShouldThrowIfUserIsntPresent(){
        try (SeContainer container = initializer.initialize()) {

            AuthenticationFacade authenticationFacade = container.select(AuthenticationFacade.class).get();

            LoginCommand loginCommand = LoginCommand
                    .builder()
                    .username("unknown")
                    .clearTxtPassword("1234")
                    .build();

            assertThrows(LoginFailedException.class, () -> {
                authenticationFacade.login(loginCommand);
            });
        }
    }

    @Test
    @Order(5)
    public void itShouldThrowIfUserIsPresentButPasswordIncorrect() {
        try (SeContainer container = initializer.initialize()) {

            AuthenticationFacade authenticationFacade = container.select(AuthenticationFacade.class).get();

            LoginCommand loginCommand = LoginCommand
                    .builder()
                    .username("henri")
                    .clearTxtPassword("unknown")
                    .build();

            assertThrows(LoginFailedException.class, () -> {
                authenticationFacade.login(loginCommand);
            });
        }
    }

    @Test
    @Order(6)
    public void itShouldThrowIfPasswordAndConfirmationPasswordArentTheSame(){
        try (SeContainer container = initializer.initialize()) {

            AuthenticationFacade authenticationFacade = container.select(AuthenticationFacade.class).get();

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
        }
    }

}
 */
