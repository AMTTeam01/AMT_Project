package ch.heigvd.amt.mvcProject.application.authentication;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.UsersDTO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;
/*
@RunWith(Arquillian.class)
public class AuthenticationFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Before
    public void init() throws RegistrationFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("patrick")
                .email("patrick@gmail.com")
                .build();

        authenticationFacade.register(registerCommand);

        registerCommand = RegisterCommand.builder()
                .username("henri")
                .email("henri@gmail.com")
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .build();

        authenticationFacade.register(registerCommand);
    }

    @After
    public void clean() {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        // Get all users

        UsersDTO usersDTO = userFacade.getUsers();

        for(UsersDTO.UserDTO user : usersDTO.getUsers()) {
            userFacade.removeUser(user.getId());
        }
    }

    @Test
    @Order(1)
    public void itShouldRegisterANewUser() throws RegistrationFailedException, LoginFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand.builder()
                .username("chau")
                .email("chau@gmail.com")
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .build();

        authenticationFacade.register(registerCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .username("chau")
                .clearTxtPassword("1234")
                .build();

        CurrentUserDTO currentUserDTO = authenticationFacade.login(loginCommand);

        assertEquals(currentUserDTO.getUsername(), "chau");
        assertEquals(currentUserDTO.getEmail(), "chau@gmail.com");
    }

    @Test
    @Order(2)
    public void itShouldntRegisterAUserIfPresent() throws RegistrationFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("patrick")
                .email("patrick@gmail.com")
                .build();

        authenticationFacade.register(registerCommand);

        assertThrows(RegistrationFailedException.class, () -> {
            authenticationFacade.register(registerCommand);
        });
    }

    @Test
    @Order(3)
    public void itShouldLoginAUserIfPresent() throws LoginFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

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
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

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
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

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
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("12346")
                .username("chau")
                .email("chau@gmail.com")
                .build();

        assertThrows(RegistrationFailedException.class, () -> {
            authenticationFacade.register(registerCommand);
        });
    }

}*/
