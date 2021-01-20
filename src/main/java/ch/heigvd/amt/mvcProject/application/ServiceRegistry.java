package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.APIUtils;
import ch.heigvd.amt.mvcProject.application.answer.AnswerFacade;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.comment.CommentFacade;
import ch.heigvd.amt.mvcProject.application.gamificationapi.badge.BadgeFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.domain.answer.IAnswerRepository;
import ch.heigvd.amt.mvcProject.domain.comment.ICommentRepository;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Reference a set of services
 */
@ApplicationScoped
public class ServiceRegistry {

    @Inject
    @Named("APIUtils")
    APIUtils apiUtils;

    // Users
    @Inject
    @Named("JdbcUserRepository")
    IUserRepository userRepository;
    private AuthenticationFacade authenticationFacade;
    private UserFacade userFacade;

    // Questions
    @Inject
    @Named("JdbcQuestionRepository")
    IQuestionRepository questionRepository;
    private QuestionFacade questionFacade;

    // Answer
    @Inject
    @Named("JdbcAnswerRepository")
    IAnswerRepository answerRepository;
    private AnswerFacade answerFacade;

    // Comment
    @Inject
    @Named("JdbcCommentRepository")
    ICommentRepository commentRepository;
    private CommentFacade commentFacade;

    private BadgeFacade badgeFacade;

    public ServiceRegistry() {
    }

    @PostConstruct
    private void setup() {
        authenticationFacade = new AuthenticationFacade(userRepository);
        userFacade = new UserFacade(userRepository);
        commentFacade = new CommentFacade(commentRepository, userFacade);
        questionFacade = new QuestionFacade(questionRepository, userFacade, commentFacade, apiUtils);
        answerFacade = new AnswerFacade(answerRepository, userFacade, questionFacade, commentFacade);
        badgeFacade = new BadgeFacade(apiUtils);
    }

    public AuthenticationFacade getAuthenticationFacade() {
        return authenticationFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public AnswerFacade getAnswerFacade() {
        return answerFacade;
    }

    public CommentFacade getCommentFacade() {
        return commentFacade;
    }

    public BadgeFacade getBadgeFacade() {
        return badgeFacade;
    }
}

