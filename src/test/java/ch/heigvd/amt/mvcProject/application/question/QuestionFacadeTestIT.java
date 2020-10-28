package ch.heigvd.amt.mvcProject.application.question;


import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.AnswerCommand;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFailedException;
import ch.heigvd.amt.mvcProject.application.answer.AnswerQuery;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.Answer;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.question.Question;
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
import org.junit.jupiter.api.AfterAll;
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
    private UserFacade userFacade;
    private QuestionFacade questionFacade;
    private AnswerFacade answerFacade;


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

        userFacade = serviceRegistry.getUserFacade();

        answerFacade = serviceRegistry.getAnswerFacade();

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
    public void cleanUp() throws QuestionFailedException, UserFailedException, AnswerFailedException {
        userFacade.removeUser(currentUserDTO.getUserId());

        // Clean all questions
        QuestionsDTO questionDTO = questionFacade.getQuestions();
        for(QuestionsDTO.QuestionDTO question : questionDTO.getQuestions()) {
            questionFacade.removeQuestion(question.getId());
        }
    }

    @Test
    public void addQuestionShouldWork() throws QuestionFailedException, UserFailedException, AnswerFailedException {

        int sizeBefore = questionFacade.getQuestions().getQuestions().size();

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getQuestions();
        assertNotNull(view);


        assertEquals(sizeBefore + 1, view.getQuestions().size());
        assertEquals(command.getTitle(), view.getQuestions().get(0).getTitle());

        questionFacade.removeQuestion(question.getId());
    }


    @Test
    public void getQuestionByIdShouldWork() throws QuestionFailedException, UserFailedException, AnswerFailedException {

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        QuestionsDTO.QuestionDTO questionInRepo = questionFacade
                .getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertNotNull(questionInRepo);

        questionFacade.removeQuestion(question.getId());
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
    public void removeQuestion_ShouldRemoveQuestion_WhenCalled()
            throws QuestionFailedException, UserFailedException, AnswerFailedException {

        int sizeBefore = questionFacade.getQuestions().getQuestions().size();

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();


        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        AnswerCommand answerCommand = AnswerCommand.builder()
                .userId(currentUserDTO.getUserId())
                .creationDate(new Date())
                .description("Bla")
                .questionId(question.getId())
                .build();

        answerFacade.addAnswer(answerCommand);

        QuestionId questionId = question.getId();

        questionFacade.removeQuestion(question.getId());

        QuestionsDTO view = questionFacade.getQuestions();
        assertNotNull(view);
        assertEquals(sizeBefore, view.getQuestions().size());

        assertThrows(QuestionFailedException.class, () -> {
            answerFacade.getAnswers(AnswerQuery.builder().questionId(questionId).build());
        });
    }

    @Test
    public void delete_ShouldThrownError_IfQuestionIdDoesntExist() {

        assertThrows(QuestionFailedException.class, () -> {
            questionFacade.removeQuestion(new QuestionId());
        });

    }

    @Test
    public void getQuestionById_ShouldReturnQuestion_WhenACorrectIdWasPassed()
            throws QuestionFailedException, UserFailedException, AnswerFailedException {

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(command.getDescription(), question.getDescription());
        assertEquals(command.getTitle(), question.getTitle());
        assertEquals(command.getUserId(), question.getUserId());
        assertEquals(command.getCreationDate(), question.getCreationDate());

        questionFacade.removeQuestion(question.getId());

    }

    @Test
    public void getQuestionById_ShouldThrownError_IfQuestionIdDoesntExist() {
        assertThrows(QuestionFailedException.class, () -> {
            questionFacade.getQuestion(QuestionQuery.builder().questionId(new QuestionId()).build());
        });
    }

    @Test
    public void upvoteQuestionShouldWork() throws UserFailedException, QuestionFailedException, AnswerFailedException, InterruptedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        questionFacade.upvote(currentUserDTO.getUserId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(1, upvotedQuestion.getVotes());

        questionFacade.removeQuestion(upvotedQuestion.getId());
    }

    @Test
    public void upvotingTwiceRemovesTheUpvote() throws UserFailedException, QuestionFailedException, AnswerFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        questionFacade.upvote(currentUserDTO.getUserId(), question.getId());
        questionFacade.upvote(currentUserDTO.getUserId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(0, upvotedQuestion.getVotes());

        questionFacade.removeQuestion(upvotedQuestion.getId());
    }

    @Test
    public void upvotingAndDownvotingShouldNotChangeTheVotes() throws UserFailedException, QuestionFailedException, AnswerFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        int initVotes = question.getVotes();

        questionFacade.upvote(currentUserDTO.getUserId(), question.getId());
        questionFacade.downvote(currentUserDTO.getUserId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(initVotes, upvotedQuestion.getVotes());

        questionFacade.removeQuestion(upvotedQuestion.getId());
    }

    @Test
    public void upvotingWithMultipleUsersShouldGiveMultipleVotes() throws UserFailedException, QuestionFailedException, RegistrationFailedException, AnswerFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        // Create 2 new users
        RegisterCommand registerCommand1 = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("steph")
                .email("steph@gmail.com")
                .build();
        RegisterCommand registerCommand2 = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("john")
                .email("john@gmail.com")
                .build();
        User u1 = authenticationFacade.register(registerCommand1);
        User u2 = authenticationFacade.register(registerCommand2);

        questionFacade.upvote(currentUserDTO.getUserId(), question.getId());
        questionFacade.upvote(u1.getId(), question.getId());
        questionFacade.upvote(u2.getId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(3, upvotedQuestion.getVotes());

        questionFacade.removeQuestion(upvotedQuestion.getId());
        userFacade.removeUser(u1.getId());
        userFacade.removeUser(u2.getId());
    }

    @Test
    public void downvotingWithMultipleUsersShouldGiveNegativeVotes() throws UserFailedException, QuestionFailedException, RegistrationFailedException, AnswerFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        // Create 2 new users
        RegisterCommand registerCommand1 = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("simon")
                .email("simon@gmail.com")
                .build();
        RegisterCommand registerCommand2 = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("johnny")
                .email("johnny@gmail.com")
                .build();
        User u1 = authenticationFacade.register(registerCommand1);
        User u2 = authenticationFacade.register(registerCommand2);

        questionFacade.downvote(currentUserDTO.getUserId(), question.getId());
        questionFacade.downvote(u1.getId(), question.getId());
        questionFacade.downvote(u2.getId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(-3, upvotedQuestion.getVotes());

        questionFacade.removeQuestion(upvotedQuestion.getId());
        userFacade.removeUser(u1.getId());
        userFacade.removeUser(u2.getId());
    }
}
