package ch.heigvd.amt.mvcProject.application.authentication;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.Current;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(Arquillian.class)
public class AuthenticationFacadeTest {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Test
    public void itShouldRegisterANewUser() throws RegistrationFailedException, LoginFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand.builder()
                .username("henri")
                .email("henri@gmail.com")
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .build();

        authenticationFacade.register(registerCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .username("henri")
                .clearTxtPassword("1234")
                .build();

        CurrentUserDTO currentUserDTO = authenticationFacade.login(loginCommand);

        assertEquals(currentUserDTO.getUsername(), "henri");
        assertEquals(currentUserDTO.getEmail(), "henri@gmail.com");
    }

    /*
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
