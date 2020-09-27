package ch.heigvd.amt.mvcProject.application.authentication.register;

import ch.heigvd.amt.mvcProject.application.BusinessException;
import lombok.Value;

public class RegistrationFailedException extends BusinessException {
    public RegistrationFailedException(String message){
        super(message);
    }
}
