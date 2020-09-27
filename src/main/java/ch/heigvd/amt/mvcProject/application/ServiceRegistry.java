package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.user.UserFacade;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.domain.user.UserId;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryUserRepository;

/**
 * Reference a set of services
 */
public class ServiceRegistry{

    private static ServiceRegistry singleton; // Code smell

    private IUserRepository userRepository;
    private UserFacade userFacade;

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
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public boolean isUserExist(UserId userId) {
        return userRepository.isUserExist(userId);
    }

    public boolean isUserExist(String username, String password) {
        return userRepository.isUserExist(username, password);
    }
}

