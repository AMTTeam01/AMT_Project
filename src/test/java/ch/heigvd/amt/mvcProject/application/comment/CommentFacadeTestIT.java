package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.answer.*;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginCommand;
import ch.heigvd.amt.mvcProject.application.authentication.login.LoginFailedException;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegisterCommand;
import ch.heigvd.amt.mvcProject.application.authentication.register.RegistrationFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionCommand;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFailedException;
import ch.heigvd.amt.mvcProject.application.question.QuestionsDTO;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
import ch.heigvd.amt.mvcProject.domain.answer.AnswerId;
import ch.heigvd.amt.mvcProject.domain.comment.Comment;
import ch.heigvd.amt.mvcProject.domain.comment.CommentId;
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

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CommentFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    private CurrentUserDTO currentUserDTO;

    private final static String USERNAME = "commentFacade";
    private final static String EMAIL = USERNAME + "@heig.ch";
    private final static String PWD = "1234";

    private CommentFacade commentFacade;
    private UserFacade userFacade;
    private QuestionFacade questionFacade;
    private AnswerFacade answerFacade;

    private QuestionsDTO.QuestionDTO question;
    private AnswersDTO.AnswerDTO answer;

    @Inject
    ServiceRegistry serviceRegistry;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt");
        return archive;
    }

    @Before
    public void init()
            throws RegistrationFailedException, LoginFailedException, UserFailedException, QuestionFailedException,
            CommentFailedException, AnswerFailedException {

        AuthenticationFacade authenticationFacade = serviceRegistry.getAuthenticationFacade();
        commentFacade = serviceRegistry.getCommentFacade();
        questionFacade = serviceRegistry.getQuestionFacade();
        userFacade = serviceRegistry.getUserFacade();
        answerFacade = serviceRegistry.getAnswerFacade();

        // Create a new user
        RegisterCommand registerCommand = RegisterCommand.builder()
                .email(EMAIL)
                .confirmationClearTxtPassword(PWD)
                .clearTxtPassword(PWD)
                .username(USERNAME)
                .build();

        authenticationFacade.register(registerCommand);

        // Login as the new user created
        LoginCommand loginCommand = LoginCommand.builder()
                .clearTxtPassword(PWD)
                .username(USERNAME)
                .build();

        currentUserDTO = authenticationFacade.login(loginCommand);

        // Create a new question
        QuestionCommand questionCommand = QuestionCommand.builder()
                .userId(currentUserDTO.getUserId())
                .title("Comment Facade Test Question")
                .description("Test")
                .creationDate(new Date())
                .build();

        question = questionFacade.addQuestion(questionCommand);

        // Create a answer that response the question created
        AnswerCommand answerCommand = AnswerCommand.builder()
                .questionId(question.getId())
                .description("Answer Test")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        answer = answerFacade.addAnswer(answerCommand);
    }

    @After
    public void cleanUp() throws AnswerFailedException, QuestionFailedException {
        // Delete answer
        answerFacade.removeAnswer(answer.getId());

        // Delete question
        questionFacade.removeQuestion(question.getId());

        // Delete user
        userFacade.removeUser(currentUserDTO.getUserId());
    }


    @Test
    public void addComment_ShouldAddACommentInRepo_WhenAAnswerIdIsInTheQuery()
            throws UserFailedException, CommentFailedException, QuestionFailedException, AnswerFailedException {

        CommentCommand command = CommentCommand.builder()
                .answerId(answer.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();

        CommentsDTO.CommentDTO comment = commentFacade.addComment(command);

        assertEquals(1, commentFacade.getComments(CommentQuery.builder()
                .answerId(answer.getId()).build()).getComments().size());

        commentFacade.removeComment(comment.getId());
    }


    @Test
    public void addComment_ShouldAddACommentInRepo_WhenAQuestionIdIsInTheQuery()
            throws UserFailedException, CommentFailedException, QuestionFailedException, AnswerFailedException {

        CommentCommand command = CommentCommand.builder()
                .questionId(question.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();

        CommentsDTO.CommentDTO comment = commentFacade.addComment(command);

        assertEquals(1, commentFacade.getComments(CommentQuery.builder()
                .questionId(question.getId()).build()).getComments().size());

        commentFacade.removeComment(comment.getId());
    }

    public void addComment_ShouldThrowAError_WhenAQuestionIdAndAnswerIdAreInTheQuery() {

        CommentCommand command = CommentCommand.builder()
                .questionId(question.getId())
                .answerId(answer.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();


        assertThrows(CommentFailedException.class,
                () -> commentFacade.addComment(command)
        );
    }

    public void addComment_ShouldThrowAError_WhenTheUserDoesntExistInTheRepo() {

        CommentCommand command = CommentCommand.builder()
                .questionId(question.getId())
                .answerId(answer.getId())
                .userId(new UserId())
                .createDate(new Date())
                .description("Test comment")
                .build();


        assertThrows(UserFailedException.class,
                () -> commentFacade.addComment(command)
        );
    }


    @Test
    public void getComments_ShouldReturnExpectingComment_WhenQuestionIdIsPassed()
            throws UserFailedException, CommentFailedException, QuestionFailedException, AnswerFailedException {
        CommentCommand command = CommentCommand.builder()
                .questionId(question.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();

        CommentsDTO.CommentDTO c1 = commentFacade.addComment(command);

        CommentsDTO commentsDTO = commentFacade.getComments(CommentQuery.builder()
                .questionId(question.getId())
                .build());

        assertEquals(1, commentsDTO.getComments().size());

        assertEquals(c1.getId(), commentsDTO.getComments().get(0).getId());

        commentFacade.removeComment(c1.getId());
    }


    @Test
    public void getComments_ShouldReturnExpectingComment_WhenAnswerIdIsPassed()
            throws UserFailedException, CommentFailedException, QuestionFailedException, AnswerFailedException {

        CommentCommand command = CommentCommand.builder()
                .answerId(answer.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();


        CommentsDTO.CommentDTO c1 = commentFacade.addComment(command);

        CommentsDTO commentsDTO = commentFacade.getComments(CommentQuery.builder()
                .answerId(answer.getId())
                .build());

        assertEquals(1, commentsDTO.getComments().size());

        assertEquals(c1.getId(), commentsDTO.getComments().get(0).getId());

        commentFacade.removeComment(c1.getId());
    }

    @Test
    public void getComments_ShouldThrowError_IfAnswerIdIsNotInRepo() {

        assertThrows(AnswerFailedException.class,
                () -> commentFacade.getComments(CommentQuery.builder().answerId(new AnswerId()).build())
        );
    }

    @Test
    public void getComments_ShouldThrowError_IfQuestionIdIsNotInRepo() {

        assertThrows(QuestionFailedException.class,
                () -> commentFacade.getComments(CommentQuery.builder().questionId(new QuestionId()).build())
        );
    }

    @Test
    public void getComments_ShouldReturnAllComment_WhenNoParamWasPassed()
            throws UserFailedException, CommentFailedException {

        CommentCommand commandAnswer = CommentCommand.builder()
                .answerId(answer.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();

        CommentsDTO.CommentDTO answerComment = commentFacade.addComment(commandAnswer);

        CommentCommand commandQuestion = CommentCommand.builder()
                .questionId(question.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();

        CommentsDTO.CommentDTO questionComment = commentFacade.addComment(commandQuestion);

        CommentsDTO view = commentFacade.getComments();

        assertEquals(2, view.getComments().size());

        commentFacade.getComment(CommentQuery.builder().commentId(answerComment.getId()).build());
        commentFacade.getComment(CommentQuery.builder().commentId(questionComment.getId()).build());

        commentFacade.removeComment(answerComment.getId());
        commentFacade.removeComment(questionComment.getId());
    }

    @Test
    public void removeComment_ShouldRemoveTheCommentInTheRepo_WhenAValidIdPassed()
            throws UserFailedException, CommentFailedException {
        CommentCommand command = CommentCommand.builder()
                .answerId(answer.getId())
                .userId(currentUserDTO.getUserId())
                .createDate(new Date())
                .description("Test comment")
                .build();

        CommentsDTO.CommentDTO comment = commentFacade.addComment(command);

        commentFacade.removeComment(comment.getId());

        CommentsDTO view = commentFacade.getComments();

        assertEquals(0, view.getComments().size());

        assertThrows(CommentFailedException.class,
                () -> commentFacade.getComment(CommentQuery.builder().commentId(comment.getId()).build()));
    }

    @Test
    public void removeComment_ShouldThrowAError_IfTheIdInstInTheRepo() {
        assertThrows(CommentFailedException.class,
                () -> commentFacade.removeComment(new CommentId()));
    }
}