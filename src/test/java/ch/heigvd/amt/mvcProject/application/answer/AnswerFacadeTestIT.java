package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.comment.CommentFailedException;
import ch.heigvd.amt.mvcProject.application.question.*;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.User;
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
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Arquillian.class)
public class AnswerFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    ServiceRegistry serviceRegistry;

    private AnswerFacade answerFacade;
    private AuthenticationFacade authenticationFacade;
    private QuestionFacade questionFacade;
    private UserFacade userFacade;

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
    public void init() throws RegistrationFailedException, QuestionFailedException,
            LoginFailedException, UserFailedException, AnswerFailedException, CommentFailedException {
        answerFacade = serviceRegistry.getAnswerFacade();

        authenticationFacade = serviceRegistry.getAuthenticationFacade();

        questionFacade = serviceRegistry.getQuestionFacade();

        userFacade = serviceRegistry.getUserFacade();

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
    public void addAnswer_ShouldAddAAnswerToTheQuestion_WhenCalled()
            throws AnswerFailedException, QuestionFailedException,
            UserFailedException, CommentFailedException {

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

        assertEquals(1, updatedQuestion.getAnswersDTO().getAnswers().size());

        answerFacade.removeAnswer(newAnswer.getId());
    }

    @Test
    public void andAnswer_ShouldThrowError_IfQuestionIdNotInRepo() {
        AnswerCommand command = AnswerCommand.builder()
                .questionId(new QuestionId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        assertThrows(QuestionFailedException.class, () ->
                answerFacade.addAnswer(command)
        );
    }

    @Test
    public void andAnswer_ShouldThrowError_IfUserIdNotInRepo() {
        AnswerCommand command = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(new UserId())
                .build();

        assertThrows(UserFailedException.class, () ->
                answerFacade.addAnswer(command)
        );
    }

    @Test
    public void getAnswers_ShouldReturnExpectingAnswer_WhenQuestionIdIsPassed()
            throws AnswerFailedException, UserFailedException, QuestionFailedException, CommentFailedException {
        AnswerCommand answerCommand1 = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test 1")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO a1 = answerFacade.addAnswer(answerCommand1);

        AnswerCommand answerCommand2 = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test 2")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO a2 = answerFacade.addAnswer(answerCommand2);

        AnswersDTO answersDTO = answerFacade.getAnswers(AnswerQuery.builder().questionId(newQuestion.getId()).build());

        assertEquals(2, answersDTO.getAnswers().size());

        answerFacade.removeAnswer(a1.getId());
        answerFacade.removeAnswer(a2.getId());

    }

    @Test
    public void removeAnswer_ShouldRemoveInTheRepo_WhenCalled()
            throws UserFailedException, AnswerFailedException, QuestionFailedException, CommentFailedException {

        AnswerQuery query = AnswerQuery.builder().questionId(newQuestion.getId()).build();

        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answerDTO = answerFacade.addAnswer(answerCommand);

        answerFacade.removeAnswer(answerDTO.getId());

        AnswersDTO view = answerFacade.getAnswers(query);

        assertEquals(0,view.getAnswers().size());

    }

    @Test
    public void removeAnswer_ShouldThrowError_IfAnswerIdNotInRepo() {
        assertThrows(AnswerFailedException.class,
                () -> answerFacade.removeAnswer(new AnswerId())
        );
    }

    @Test
    public void upvoteAnswerShouldWork() throws UserFailedException, QuestionFailedException, AnswerFailedException, InterruptedException, CommentFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answerDTO = answerFacade.addAnswer(answerCommand);

        answerFacade.upvote(currentUserDTO.getUserId(), answerDTO.getId());

        AnswersDTO.AnswerDTO upvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answerDTO.getId())
                .build());

        assertEquals(1, upvotedAnswer.getVotes());

        answerFacade.removeAnswer(upvotedAnswer.getId());
    }

    @Test
    public void upvotingTwiceRemovesTheUpvote() throws UserFailedException, QuestionFailedException, AnswerFailedException, CommentFailedException, RegistrationFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answerDTO = answerFacade.addAnswer(answerCommand);

        // Add a user to avoid having 0 as the expected result
        RegisterCommand registerCommand1 = RegisterCommand
                .builder()
                .clearTxtPassword("1234")
                .confirmationClearTxtPassword("1234")
                .username("gandalf")
                .email("gandalf@gmail.com")
                .build();
        User u1 = authenticationFacade.register(registerCommand1);

        answerFacade.upvote(u1.getId(), answerDTO.getId());
        answerFacade.upvote(currentUserDTO.getUserId(), answerDTO.getId());
        answerFacade.upvote(currentUserDTO.getUserId(), answerDTO.getId());

        AnswersDTO.AnswerDTO upvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answerDTO.getId())
                .build());

        assertEquals(1, upvotedAnswer.getVotes());

        answerFacade.removeAnswer(upvotedAnswer.getId());
        userFacade.removeUser(u1.getId());
    }

    /*@Test
    public void upvotingAndDownvotingShouldNotChangeTheVotes() throws UserFailedException, QuestionFailedException, AnswerFailedException, CommentFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        questionFacade.upvote(currentUserDTO.getUserId(), question.getId());
        questionFacade.downvote(currentUserDTO.getUserId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(0, upvotedQuestion.getVotes());

        questionFacade.removeQuestion(upvotedQuestion.getId());
    }

    @Test
    public void upvotingWithMultipleUsersShouldGiveMultipleVotes() throws UserFailedException, QuestionFailedException, RegistrationFailedException, AnswerFailedException, CommentFailedException {
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
    public void downvotingWithMultipleUsersShouldGiveNegativeVotes() throws UserFailedException, QuestionFailedException, RegistrationFailedException, AnswerFailedException, CommentFailedException {
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
    }*/
}
