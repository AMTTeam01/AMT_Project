package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.comment.CommentFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.Question;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc.JdbcQuestionRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc.JdbcUserRepository;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Reference a set of services
 */
@ApplicationScoped
public class ServiceRegistry {

    // Users
    @Inject @Named("JdbcUserRepository")
    IUserRepository userRepository;
    private AuthenticationFacade authenticationFacade;
    private UserFacade userFacade;

    // Questions
    @Inject @Named("JdbcQuestionRepository")
    IQuestionRepository questionRepository;
    private QuestionFacade questionFacade;

    // Answer
    @Inject @Named("JdbcAnswerRepository")
    IAnswerRepository answerRepository;
    private AnswerFacade answerFacade;

    @Inject @Named("JdbcCommentRepository")
    ICommentRepository commentRepository;
    private CommentFacade commentFacade;

    public ServiceRegistry() {
    }

    @PostConstruct
    private void setup() {
        authenticationFacade = new AuthenticationFacade(userRepository);
        userFacade = new UserFacade(userRepository);
        questionFacade = new QuestionFacade(questionRepository, userFacade);
        answerFacade = new AnswerFacade(answerRepository, userFacade, questionFacade);
        commentFacade = new CommentFacade(commentRepository);
    }

    public AuthenticationFacade getAuthenticationFacade() {
        return authenticationFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public UserFacade getUserFacade(){
        return userFacade;
    }

    public AnswerFacade getAnswerFacade(){
        return answerFacade;
    }

    public CommentFacade getCommentFacade() {
        return commentFacade;
    }
}

