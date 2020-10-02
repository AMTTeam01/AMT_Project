package java.ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.ServiceRegistry;
import ch.heigvd.amt.mvcProject.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.mvcProject.application.question.QuestionFacade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RegisterLogin {

    private static ServiceRegistry serviceRegistry;
    private static AuthenticationFacade authenticationFacade;

    @BeforeAll
    public static void init(){
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        authenticationFacade = serviceRegistry.getUserFacade();
    }

    @Test
    public void TestRegister(){

    }
}
