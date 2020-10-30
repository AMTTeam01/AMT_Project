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

import static ch.heigvd.amt.mvcProject.application.VoteUtils.DOWNVOTE;
import static ch.heigvd.amt.mvcProject.application.VoteUtils.UPVOTE;
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
    private String USERNAME_1 = "answerFacade1";
    private String EMAIL_1 = USERNAME_1 + "@heig.ch";
    private String PWD_1 = "1234";
    private String USERNAME_2 = "answerFacade2";
    private String EMAIL_2 = USERNAME_2 + "@heig.ch";
    private String PWD_2 = "1234";

    private CurrentUserDTO currentUserDTO;
    private CurrentUserDTO userDTO1;
    private CurrentUserDTO userDTO2;

    private QuestionsDTO.QuestionDTO newQuestion;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Before
    public void init()
            throws RegistrationFailedException, LoginFailedException, UserFailedException, QuestionFailedException {

        answerFacade = serviceRegistry.getAnswerFacade();
        authenticationFacade = serviceRegistry.getAuthenticationFacade();
        questionFacade = serviceRegistry.getQuestionFacade();
        userFacade = serviceRegistry.getUserFacade();

        currentUserDTO = registerAndLoginUser(USERNAME, EMAIL, PWD);
        userDTO1 = registerAndLoginUser(USERNAME_1, EMAIL_1, PWD_1);
        userDTO2 = registerAndLoginUser(USERNAME_2, EMAIL_2, PWD_2);

        QuestionCommand questionCommand = QuestionCommand.builder()
                .userId(currentUserDTO.getUserId())
                .creationDate(new Date())
                .description("TEST")
                .title("TEST")
                .build();

        newQuestion = questionFacade.addQuestion(questionCommand);
    }

    private CurrentUserDTO registerAndLoginUser(String username,
                                                String email, String password)
            throws RegistrationFailedException, LoginFailedException {
        // Create a new user
        RegisterCommand registerCommand = RegisterCommand.builder()
                .email(email)
                .confirmationClearTxtPassword(password)
                .clearTxtPassword(password)
                .username(username)
                .build();

        authenticationFacade.register(registerCommand);

        // Login as the new user created
        LoginCommand loginCommand = LoginCommand.builder()
                .clearTxtPassword(password)
                .username(username)
                .build();

        return authenticationFacade.login(loginCommand);
    }

    @After
    public void cleanUp() throws QuestionFailedException {

        questionFacade.removeQuestion(newQuestion.getId());

        authenticationFacade.delete(currentUserDTO.getUserId());
        authenticationFacade.delete(userDTO1.getUserId());
        authenticationFacade.delete(userDTO2.getUserId());

    }

    @Test
    public void addAnswer_ShouldAddAAnswerToTheQuestion_WhenCalled()
            throws AnswerFailedException, QuestionFailedException, UserFailedException, CommentFailedException {


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

        assertEquals(updatedQuestion.getAnswersDTO().getAnswers().size(), 1);

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
    public void upvoteAnswerShouldWork() throws UserFailedException, AnswerFailedException, QuestionFailedException, CommentFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answer = answerFacade.addAnswer(answerCommand);

        answerFacade.vote(currentUserDTO.getUserId(), answer.getId(), UPVOTE);

        AnswersDTO.AnswerDTO upvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answer.getId())
                .build());

        assertEquals(1, upvotedAnswer.getVotes());

        answerFacade.removeAnswer(upvotedAnswer.getId());
    }

    @Test
    public void downvoteAnswerShouldWork() throws UserFailedException, QuestionFailedException, AnswerFailedException, InterruptedException, CommentFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answer = answerFacade.addAnswer(answerCommand);

        answerFacade.vote(currentUserDTO.getUserId(), answer.getId(), DOWNVOTE);

        AnswersDTO.AnswerDTO downvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answer.getId())
                .build());

        assertEquals(-1, downvotedAnswer.getVotes());

        answerFacade.removeAnswer(downvotedAnswer.getId());
    }


    @Test
    public void upvotingTwiceRemovesTheUpvote() throws UserFailedException, QuestionFailedException, AnswerFailedException, CommentFailedException, RegistrationFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answer = answerFacade.addAnswer(answerCommand);

        // to avoid having 0 as the expected value
        answerFacade.vote(userDTO1.getUserId(), answer.getId(), UPVOTE);

        answerFacade.vote(currentUserDTO.getUserId(), answer.getId(), UPVOTE);
        answerFacade.vote(currentUserDTO.getUserId(), answer.getId(), UPVOTE);

        AnswersDTO.AnswerDTO upvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answer.getId())
                .build());

        assertEquals(1, upvotedAnswer.getVotes());

        answerFacade.removeAnswer(upvotedAnswer.getId());
    }

    @Test
    public void upvotingWithMultipleUsersShouldGiveMultipleVotes() throws UserFailedException, QuestionFailedException, RegistrationFailedException, AnswerFailedException, CommentFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answer = answerFacade.addAnswer(answerCommand);

        // to avoid having 0 as the expected value
        answerFacade.vote(userDTO1.getUserId(), answer.getId(), UPVOTE);
        answerFacade.vote(userDTO2.getUserId(), answer.getId(), UPVOTE);
        answerFacade.vote(currentUserDTO.getUserId(), answer.getId(), UPVOTE);

        AnswersDTO.AnswerDTO upvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answer.getId())
                .build());

        assertEquals(3, upvotedAnswer.getVotes());

        answerFacade.removeAnswer(upvotedAnswer.getId());
    }

    @Test
    public void downvotingWithMultipleUsersShouldGiveNegativeVotes() throws UserFailedException, QuestionFailedException, RegistrationFailedException, AnswerFailedException, CommentFailedException {
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(newQuestion.getId())
                .description("Answer test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        AnswersDTO.AnswerDTO answer = answerFacade.addAnswer(answerCommand);

        answerFacade.vote(userDTO1.getUserId(), answer.getId(), DOWNVOTE);
        answerFacade.vote(userDTO2.getUserId(), answer.getId(), DOWNVOTE);
        answerFacade.vote(currentUserDTO.getUserId(), answer.getId(), DOWNVOTE);

        AnswersDTO.AnswerDTO upvotedAnswer = answerFacade.getAnswer(AnswerQuery.builder()
                .questionId(answerCommand.getQuestionId())
                .answerId(answer.getId())
                .build());

        assertEquals(-3, upvotedAnswer.getVotes());

        answerFacade.removeAnswer(upvotedAnswer.getId());
    }
}
