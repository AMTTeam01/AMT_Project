package ch.heigvd.amt.mvcProject.application.authentication;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.AnswerCommand;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class AnswerFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    private AnswerFacade answerFacade;

    private final static String USERNAME = "answerFacade";
    private final static String EMAIL = USERNAME + "@heig.ch";
    private final static String PWD = "1234";

    private final static QuestionId QUESTION_ID = new QuestionId();

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Before
    public void init() throws RegistrationFailedException {
        answerFacade = serviceRegistry.getAnswerFacade();

        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();

        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(USERNAME)
                .clearTxtPassword(PWD)
                .confirmationClearTxtPassword(PWD)
                .email(EMAIL)
                .build();

        authenticationFacade.register(registerCommand);

        QuestionFacade questionFacade = serviceRegistry.getQuestionFacade();

        QuestionCommand questionCommand = QuestionCommand.builder()
                .build();
    }

    @Test
    public void itShouldAddAAnswer(){

        AnswerCommand answerCommand = AnswerCommand.builder()
                .build();
    }
}
