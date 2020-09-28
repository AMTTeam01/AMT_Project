package ch.heigvd.amt.mvcProject.application;

import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.domain.user.IUserRepository;
import ch.heigvd.amt.mvcProject.infrastructure.persistence.memory.InMemoryUserRepository;

/**
 * Reference a set of services
 */
public class ServiceRegistry{

    private static ServiceRegistry singleton; // Code smell

    private IUserRepository userRepository;
    private AuthenticationFacade authenticationFacade;

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
    }

    public AuthenticationFacade getUserFacade() {
        return authenticationFacade;
    }

}

