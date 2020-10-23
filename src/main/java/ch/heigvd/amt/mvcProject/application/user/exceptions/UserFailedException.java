package ch.heigvd.amt.mvcProject.application.user.exceptions;

import ch.heigvd.amt.mvcProject.application.BusinessException;

public class UserFailedException extends BusinessException {

    public UserFailedException(String message) {
        super(message);
    }
}
