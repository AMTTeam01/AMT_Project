package ch.heigvd.amt.mvcProject.application.user;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.user.edit.EditFailedException;
import ch.heigvd.amt.mvcProject.application.user.edit.EditUserCommand;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Stereotype;
import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    private final String username = "chau";
    private final String password = "1234";
    private final String email = "chau@gmail.com";
    private final String newUsername = "patrick";
    private final String newEmail = "patrick@gmail.com";
    private final String newPassword = "5678";
    private UserId currentId = null;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    /*

    @Test
    @Order(1)
    public void itShouldGetAllUsersIfQueryIsNull() throws RegistrationFailedException, LoginFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();
        UserFacade userFacade = serviceRegistry.getUserFacade();

        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword(password)
                .confirmationClearTxtPassword(password)
                .username(username)
                .email(email)
                .build();

        authenticationFacade.register(registerCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .clearTxtPassword(password)
                .username(username)
                .build();

        CurrentUserDTO currentUserDTO = authenticationFacade.login(loginCommand);

        registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("henri")
                .email("henri@gmail.com")
                .build();

        authenticationFacade.register(registerCommand);

        UsersDTO users = userFacade.getUsers();

        assertEquals(users.getUsers().size(), 2);
        assertEquals(users.getUsers().get(0), UsersDTO.UserDTO.builder()
                .id(currentUserDTO.getUserId())
                .username(username)
                .email(email)
                .build());
    }

    @Test
    @Order(2)
    public void itShouldEditOnlyTheUsernameIfOnlyTheUsernameIsProvided() throws EditFailedException {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentId.asString())
                .username(newUsername)
                .build();

        CurrentUserDTO currentUserDTO = userFacade.editUser(editUserCommand);

        assertEquals(currentUserDTO.getUsername(), newUsername);
    }

    @Test
    @Order(3)
    public void itShouldEditOnlyTheEmailIfOnlyTheEmailIsProvided() throws EditFailedException {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentId.asString())
                .email(newEmail)
                .build();

        CurrentUserDTO currentUserDTO = userFacade.editUser(editUserCommand);

        assertEquals(currentUserDTO.getEmail(), newEmail);
    }

    @Test
    @Order(4)
    public void itShouldEditOnlyThePasswordIfTheNewPasswordIsProvided() throws EditFailedException, LoginFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentId.asString())
                .password(newPassword)
                .confirmationPassword(newPassword)
                .build();

        userFacade.editUser(editUserCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .username(newUsername)
                .clearTxtPassword(newPassword)
                .build();

        CurrentUserDTO currentUserDTO = authenticationFacade.login(loginCommand);

        assertEquals(currentUserDTO.getUsername(), newUsername);
    }

    @Test
    @Order(5)
    public void itShouldThrowAnExceptionIfThePasswordAndTheConfirmationOfPasswordArentTheSame() {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentId.asString())
                .password(newPassword)
                .confirmationPassword(newPassword + "otherThing")
                .build();

        assertThrows(EditFailedException.class, () -> userFacade.editUser(editUserCommand));
    }

    @Test
    @Order(6)
    public void itShouldThrowAnExceptionIfTheIdIsNull() {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .email("liechti@gmail.com")
                .build();

        assertThrows(EditFailedException.class, () -> userFacade.editUser(editUserCommand));
    }*/
}
