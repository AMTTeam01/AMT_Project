package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.domain.question.IQuestionRepository;
import ch.heigvd.amt.mvcProject.domain.question.QuestionId;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryUserRepository;

/**
 * Reference a set of services
 */
public class ServiceRegistry{

    private static ServiceRegistry singleton; // Code smell

    // Users
    private IUserRepository userRepository;
    private UserFacade userFacade;

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
        userFacade = new UserFacade(userRepository);
        questionRepository = new InMemoryQuestionRepository();
        questionFacade = new QuestionFacade(questionRepository);
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public boolean hasUser(UserId userId) {
        return userRepository.hasUser(userId);
    }

    public boolean hasUser(String username, String password) {
        return userRepository.hasUser(username, password);
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

