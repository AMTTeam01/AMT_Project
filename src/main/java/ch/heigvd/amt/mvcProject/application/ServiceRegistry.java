package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
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

    // Questions
    @Inject @Named("JdbcQuestionRepository")
    IQuestionRepository questionRepository;
    private QuestionFacade questionFacade;

    public ServiceRegistry() {
    }

    @PostConstruct
    private void setup() {
        authenticationFacade = new AuthenticationFacade(userRepository);
        questionFacade = new QuestionFacade(questionRepository, userRepository);
    }

    public AuthenticationFacade getAuthenticationFacade() {
        return authenticationFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

}

