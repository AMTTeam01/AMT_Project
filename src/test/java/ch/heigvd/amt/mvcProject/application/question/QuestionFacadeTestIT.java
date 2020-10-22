package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.login.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
// TODO remove each insertion in DB => FixMethodOrder can't be removed
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    private CurrentUserDTO currentUserDTO;

    private final static String USERNAME = "questionFacade";
    private final static String EMAIL = USERNAME + "@heig.ch";
    private final static String PWD = "1234";

    private AuthenticationFacade authenticationFacade;
    private QuestionFacade questionFacade;


    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }


    @Inject
    ServiceRegistry serviceRegistry;


    @Before
    public void init() throws RegistrationFailedException, LoginFailedException {
        authenticationFacade = serviceRegistry.getAuthenticationFacade();

        questionFacade = serviceRegistry.getQuestionFacade();

        RegisterCommand registerCommand = RegisterCommand.builder()
                .email(EMAIL)
                .confirmationClearTxtPassword(PWD)
                .clearTxtPassword(PWD)
                .username(USERNAME)
                .build();

        authenticationFacade.register(registerCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .clearTxtPassword(PWD)
                .username(USERNAME)
                .build();

        currentUserDTO = authenticationFacade.login(loginCommand);

    }

    @After
    public void cleanUp() {

        authenticationFacade.delete(currentUserDTO.getUserId());
    }


    @Test
    public void GetQuestionWhenEmptyReturnEmptyList() {

        assertEquals(0, questionFacade.getQuestions(null).getQuestions().size());
    }

    @Test
    public void addQuestionShouldWork() throws QuestionFailedException {

        int sizeBefore = questionFacade.getQuestions(null).getQuestions().size();

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getQuestions(null);
        assertNotNull(view);


        assertEquals(sizeBefore + 1, view.getQuestions().size());
        assertEquals(command.getTitle(), view.getQuestions().get(0).getTitle());

        questionFacade.delete(question.getId());
    }


    @Test
    public void getQuestionByIdShouldWork() throws QuestionFailedException {

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getQuestions(null);
        QuestionId id = view.getQuestions().get(0).getId();
        QuestionQuery query = QuestionQuery.builder()
                .questionId(id)
                .build();

        QuestionsDTO.QuestionDTO viewID = questionFacade.getQuestionById(query);
        assertEquals(id, viewID.getId());

        questionFacade.delete(question.getId());
    }

    @Test
    public void addQuestion_ShouldThrowError_IfUserDoesntExist() {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(new UserId())
                .build();

        assertThrows(QuestionFailedException.class, () -> {
            questionFacade.addQuestion(command);
        });
    }

    @Test
    public void remove_ShouldRemoveQuestion_WhenCalled() throws QuestionFailedException {
        int sizeBefore = questionFacade.getQuestions(null).getQuestions().size();

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        questionFacade.delete(question.getId());

        QuestionsDTO view = questionFacade.getQuestions(null);
        assertNotNull(view);


        assertEquals(sizeBefore , view.getQuestions().size());
    }

    //TODO Test remove if question id invalid

}
