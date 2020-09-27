package ch.heigvd.amt.mvcProject.application.authentication.register;

import ch.heigvd.amt.mvcProject.application.BusinessException;
import lombok.Value;

public class RegistrationFailledException extends BusinessException {
    public RegistrationFailledException(String message){
        super(message);
    }
}
