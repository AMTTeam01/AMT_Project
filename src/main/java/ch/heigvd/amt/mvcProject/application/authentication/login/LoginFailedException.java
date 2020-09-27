package ch.heigvd.amt.mvcProject.application.authentication.login;

import ch.heigvd.amt.mvcProject.application.BusinessException;
import lombok.Value;

public class LoginFailedException extends BusinessException {
    public LoginFailedException(String message) {
        super(message);
    }
}
