package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryUserRepository;

/**
 * Reference a set of services
 */
public class ServiceRegistry{

    private static ServiceRegistry singleton; // Code smell

    // Users
    private IUserRepository userRepository;
    private AuthenticationFacade authenticationFacade;

    // Questions
    private IQuestionRepository questionRepository;
    private QuestionFacade questionFacade;

    public static ServiceRegistry getServiceRegistry(){
        if(singleton == null){
            singleton = new ServiceRegistry();
        }

        return singleton;
    }

    public ServiceRegistry() {
        singleton = this;
        userRepository = new InMemoryUserRepository();
        authenticationFacade = new AuthenticationFacade(userRepository);
        questionRepository = new InMemoryQuestionRepository();
        questionFacade = new QuestionFacade(questionRepository);

    }

    public AuthenticationFacade getUserFacade() {
        return authenticationFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public boolean hasQuestion(QuestionId questionId) {
        return questionRepository.hasQuestion(questionId);
    }

    public boolean hasQuestion(String title) {
        return questionRepository.hasQuestion(title);
    }

}

