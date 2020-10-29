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
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    private final String username = "chau";
    private final String usernameOther = "bastien";
    private final String password = "1234";
    private final String email = "chau@gmail.com";
    private final String emailOther = "bastien@gmail.com";
    private final String newUsername = "patrick";
    private final String newEmail = "patrick@gmail.com";
    private final String newPassword = "5678";
    private CurrentUserDTO currentUserDTO = null;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Before
    public void initBeforeEach() throws RegistrationFailedException, LoginFailedException {

        // Create the user Chau

        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword(password)
                .confirmationClearTxtPassword(password)
                .username(username)
                .email(email)
                .build();

        authenticationFacade.register(registerCommand);

        registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword(password)
                .confirmationClearTxtPassword(password)
                .username(usernameOther)
                .email(emailOther)
                .build();

        authenticationFacade.register(registerCommand);

        // Get the id of the user Chau

        LoginCommand loginCommand = LoginCommand.builder()
                .clearTxtPassword(password)
                .username(username)
                .build();

        currentUserDTO = authenticationFacade.login(loginCommand);
    }

    @After
    public void cleanAfterEach() {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        // Get all users

        UsersDTO usersDTO = userFacade.getUsers();

        for(UsersDTO.UserDTO user : usersDTO.getUsers()) {
            userFacade.removeUser(user.getId());
        }
    }

    @Test
    public void itShouldGetAllUsers() throws LoginFailedException, RegistrationFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        UserFacade userFacade = serviceRegistry.getUserFacade();

        // First request, only Chau is present in the database

        UsersDTO users = userFacade.getUsers();

        UsersDTO.UserDTO user = users.getUsers().get(0);

        assertEquals(users.getUsers().size(), 1);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getId().asString(), currentUserDTO.getUserId().asString());

        RegisterCommand registerCommand = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("henri")
                .email("henri@gmail.com")
                .build();

        authenticationFacade.register(registerCommand);

        // Second request, Chau and Henri are present in the database

        users = userFacade.getUsers();

        assertEquals(users.getUsers().size(), 2);

    }

    @Test
    public void itShouldEditOnlyTheUsernameIfOnlyTheUsernameIsProvided() throws EditFailedException, LoginFailedException {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentUserDTO.getUserId().asString())
                .username(newUsername)
                .email("")
                .password("")
                .confirmationPassword("")
                .build();

        currentUserDTO = userFacade.editUser(editUserCommand);

        assertEquals(currentUserDTO.getUserId(), currentUserDTO.getUserId());
        assertEquals(currentUserDTO.getUsername(), newUsername);
        assertEquals(currentUserDTO.getEmail(), email);
    }

    @Test
    public void itShouldEditOnlyTheEmailIfOnlyTheEmailIsProvided() throws EditFailedException {

        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentUserDTO.getUserId().asString())
                .email(newEmail)
                .username("")
                .password("")
                .confirmationPassword("")
                .build();

        CurrentUserDTO newUserDTO = userFacade.editUser(editUserCommand);

        assertEquals(newUserDTO.getEmail(), newEmail);
    }

    @Test
    public void itShouldEditOnlyThePasswordIfTheNewPasswordIsProvided() throws EditFailedException, LoginFailedException {
        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentUserDTO.getUserId().asString())
                .username("")
                .email("")
                .password(newPassword)
                .confirmationPassword(newPassword)
                .build();

        userFacade.editUser(editUserCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .username(username)
                .clearTxtPassword(newPassword)
                .build();

        CurrentUserDTO newUserDTO = authenticationFacade.login(loginCommand);

        assertEquals(newUserDTO.getUsername(), username);
    }

    @Test
    public void itShoulThrowIfTheUsernameIsAlreadyPressent(){
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentUserDTO.getUserId().asString())
                .password("")
                .confirmationPassword("")
                .email("")
                .username(usernameOther)
                .build();

        assertThrows(EditFailedException.class, () -> userFacade.editUser(editUserCommand));
    }

    @Test
    public void itShoulThrowIfTheEmailIsAlreadyPressent(){
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentUserDTO.getUserId().asString())
                .password("")
                .confirmationPassword("")
                .email(emailOther)
                .username("")
                .build();

        assertThrows(EditFailedException.class, () -> userFacade.editUser(editUserCommand));
    }

    @Test
    public void itShouldThrowAnExceptionIfThePasswordAndTheConfirmationOfPasswordArentTheSame() {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .id(currentUserDTO.getUserId().asString())
                .password(newPassword)
                .confirmationPassword(newPassword + "otherThing")
                .email("")
                .username("")
                .build();

        assertThrows(EditFailedException.class, () -> userFacade.editUser(editUserCommand));
    }

    @Test
    public void itShouldThrowAnExceptionIfTheIdIsNull() {
        UserFacade userFacade = serviceRegistry.getUserFacade();

        EditUserCommand editUserCommand = EditUserCommand.builder()
                .email("liechti@gmail.com")
                .username("")
                .password("")
                .confirmationPassword("")
                .build();

        assertThrows(EditFailedException.class, () -> userFacade.editUser(editUserCommand));
    }
}
