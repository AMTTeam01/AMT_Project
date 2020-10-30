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
import ch.heigvd.amt.mvcProject.application.comment.CommentFailedException;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.application.user.exceptions.UserFailedException;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QuestionFacadeTestIT {

    private final static String WARNAME = "arquillian-managed.war";

    private CurrentUserDTO currentUserDTO;
    private CurrentUserDTO userDTO1;
    private CurrentUserDTO userDTO2;

    private final static String USERNAME = "questionFacade";
    private final static String EMAIL = USERNAME + "@heig.ch";
    private final static String PWD = "1234";
    private final static String USERNAME_1 = "questionFacade1";
    private final static String EMAIL_1 = USERNAME_1 + "@heig.ch";
    private final static String PWD_1 = "1234";
    private final static String USERNAME_2 = "questionFacade2";
    private final static String EMAIL_2 = USERNAME_2 + "@heig.ch";
    private final static String PWD_2 = "1234";

    private QuestionsDTO.QuestionDTO question;

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
    public void init() throws RegistrationFailedException, LoginFailedException,
            UserFailedException, QuestionFailedException {
        authenticationFacade = serviceRegistry.getAuthenticationFacade();
        questionFacade = serviceRegistry.getQuestionFacade();
        userFacade = serviceRegistry.getUserFacade();
        answerFacade = serviceRegistry.getAnswerFacade();

        currentUserDTO = registerAndLoginUser(USERNAME, EMAIL, PWD);
        userDTO1 = registerAndLoginUser(USERNAME_1, EMAIL_1, PWD_1);
        userDTO2 = registerAndLoginUser(USERNAME_2, EMAIL_2, PWD_2);

        // Create a new question
        QuestionCommand questionCommand = QuestionCommand.builder()
                .userId(currentUserDTO.getUserId())
                .title("Comment Facade Test Question")
                .description("Test")
                .creationDate(new Date())
                .build();

        question = questionFacade.addQuestion(questionCommand);

    }

    private CurrentUserDTO registerAndLoginUser(String username, String email,
                                                String password) throws RegistrationFailedException, LoginFailedException {
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
                .clearTxtPassword(PWD)
                .username(USERNAME)
                .build();

        return authenticationFacade.login(loginCommand);
    }

    @After
    public void cleanUp() {

        userFacade.removeUser(currentUserDTO.getUserId());
    }

    @Test
    public void addQuestionShouldWork() throws QuestionFailedException, UserFailedException {

        int sizeBefore = questionFacade.getAllQuestions().getQuestions().size();

        QuestionCommand command = QuestionCommand.builder()
                .title("Titre")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        QuestionsDTO.QuestionDTO question = questionFacade.addQuestion(command);

        QuestionsDTO view = questionFacade.getAllQuestions();
        assertNotNull(view);


        assertEquals(sizeBefore + 1, view.getQuestions().size());
        assertEquals(command.getTitle(), view.getQuestions().get(0).getTitle());

        questionFacade.removeQuestion(question.getId());
    }


    @Test
    public void getQuestionByIdShouldWork() throws QuestionFailedException, UserFailedException,
            CommentFailedException, AnswerFailedException {

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
            throws QuestionFailedException, UserFailedException, AnswerFailedException, CommentFailedException {

        int sizeBefore = questionFacade.getAllQuestions().getQuestions().size();

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

        QuestionsDTO view = questionFacade.getAllQuestions();
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
            throws QuestionFailedException, UserFailedException, CommentFailedException, AnswerFailedException {

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
    public void getQuestionsWithNullQueryShouldThrowQuestionFailedException() {
        assertThrows(QuestionFailedException.class, () -> {
            questionFacade.getQuestions(null);
        });
    }

    @Test
    public void getQuestionsWithNoQueryShouldThrowQuestionFailedException() {
        assertThrows(QuestionFailedException.class, () -> {
            questionFacade.getQuestions(QuestionQuery.builder().build());
        });
    }

    @Test
    public void getQuestionsWithTitleQueryShouldGetRightQuestions() throws UserFailedException, QuestionFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("I don't know some fancy title")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        questionFacade.addQuestion(command);
        questionFacade.addQuestion(command);

        QuestionCommand command2 = QuestionCommand.builder()
                .title("Some other title")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        questionFacade.addQuestion(command2);

        QuestionsDTO questionsFromFirstCommand = questionFacade.getQuestions(QuestionQuery.builder().title(command.getTitle()).build());
        assertEquals(questionsFromFirstCommand.getQuestions().size(),2);

        QuestionsDTO questionsFromSecondCommand = questionFacade.getQuestions(QuestionQuery.builder().title(command2.getTitle()).build());
        assertEquals(questionsFromSecondCommand.getQuestions().size(),1);

        for(QuestionsDTO.QuestionDTO question : questionsFromFirstCommand.getQuestions()) {
            questionFacade.removeQuestion(question.getId());
        }
        for(QuestionsDTO.QuestionDTO question : questionsFromSecondCommand.getQuestions()) {
            questionFacade.removeQuestion(question.getId());
        }
    }

    @Test
    public void getQuestionsWithUserIdQueryShouldWork() throws UserFailedException, QuestionFailedException {
        QuestionCommand command = QuestionCommand.builder()
                .title("I don't know some fancy title")
                .description("Description")
                .creationDate(new Date())
                .userId(currentUserDTO.getUserId())
                .build();

        questionFacade.addQuestion(command);
        questionFacade.addQuestion(command);

        QuestionsDTO questions = questionFacade.getQuestions(QuestionQuery.builder().userId(command.getUserId()).build());
        assertEquals(questions.getQuestions().size(),2);

        QuestionsDTO noQuestionsHopefully = questionFacade.getQuestions(QuestionQuery.builder().userId(new UserId()).build());
        assertEquals(noQuestionsHopefully.getQuestions().size(),0);

        for(QuestionsDTO.QuestionDTO question : questions.getQuestions()) {
            questionFacade.removeQuestion(question.getId());
        }
    }

    @Test
    public void upvoteQuestionShouldWork() throws UserFailedException, QuestionFailedException, AnswerFailedException, InterruptedException, CommentFailedException {

        int initVotes = question.getRanking();

        //TODO : questionFacade.upvote(currentUserDTO.getUserId(), question.getId());

        QuestionsDTO.QuestionDTO upvotedQuestion = questionFacade.getQuestion(QuestionQuery.builder().questionId(question.getId()).build());

        assertEquals(initVotes + 1, upvotedQuestion.getRanking());

        questionFacade.removeQuestion(upvotedQuestion.getId());
    }



}
