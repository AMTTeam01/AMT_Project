package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc.JdbcQuestionRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.jdbc.JdbcUserRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryUserRepository;

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
    JdbcUserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    // Questions
    @Inject @Named("JdbcQuestionRepository")
    JdbcQuestionRepository questionRepository;
    private final QuestionFacade questionFacade;

    public ServiceRegistry() {
        System.out.println("System Registry   : ON");
        System.out.println("  - user repo     : " + userRepository);
        System.out.println("  - question repo : " + questionRepository);
        authenticationFacade = new AuthenticationFacade(userRepository);
        questionFacade = new QuestionFacade(questionRepository);
    }

    public AuthenticationFacade getAuthenticationFacade() {
        return authenticationFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

}

