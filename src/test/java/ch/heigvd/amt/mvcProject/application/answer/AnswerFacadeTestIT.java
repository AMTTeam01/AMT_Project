package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.AnswerCommand;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.question.*;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class AnswerFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    private AnswerFacade answerFacade;

    private AuthenticationFacade authenticationFacade;

    private QuestionFacade questionFacade;

    private final static String USERNAME = "answerFacade";
    private final static String EMAIL = USERNAME + "@heig.ch";
    private final static String PWD = "1234";

    private CurrentUserDTO currentUserDTO;

    private QuestionsDTO.QuestionDTO newQuestion;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Before
    public void init()
            throws RegistrationFailedException, QuestionFailedException, LoginFailedException, UserFailedException {
        answerFacade = serviceRegistry.getAnswerFacade();

        authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(USERNAME)
                .clearTxtPassword(PWD)
                .confirmationClearTxtPassword(PWD)
                .email(EMAIL)
                .build();

        authenticationFacade.register(registerCommand);

        LoginCommand loginCommand = LoginCommand.builder()
                .clearTxtPassword(PWD)
                .username(USERNAME)
                .build();

        currentUserDTO = authenticationFacade.login(loginCommand);

        questionFacade = serviceRegistry.getQuestionFacade();

        QuestionCommand questionCommand = QuestionCommand.builder()
                .userId(currentUserDTO.getUserId())
                .creationDate(new Date())
                .description("TEST")
                .title("TEST")
                .build();

        newQuestion = questionFacade.addQuestion(questionCommand);
    }

    @After
    public void cleanUp() throws QuestionFailedException {

        questionFacade.removeQuestion(newQuestion.getId());

        authenticationFacade.delete(currentUserDTO.getUserId());

    }

    @Test
    public void itShouldAddAAnswer() throws AnswerFailedException, QuestionFailedException, UserFailedException {

        int sizeBefore = newQuestion.getAnswersDTO().getAnswers().size();

        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO newAnswer = answerFacade.addAnswer(answerCommand);

        QuestionQuery query = QuestionQuery.builder()
                .questionId(newQuestion.getId()).withDetail(true).build();

        QuestionsDTO.QuestionDTO updatedQuestion = questionFacade.getQuestion(query);

        assertEquals(updatedQuestion.getAnswersDTO().getAnswers().size() - sizeBefore, 1);

        answerFacade.deleteAnswer(newAnswer.getId());
    }


}
