package ch.heigvd.amt.mvcProject.application.user.edit;

import ch.heigvd.amt.mvcProject.application.BusinessException;

public class EditFailedException extends BusinessException {
    public EditFailedException(String message) {
        super(message);
    }
}
