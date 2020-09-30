package ch.heigvd.amt.mvcProject.application.question;

import ch.heigvd.amt.mvcProject.application.BusinessException;

public class QuestionFailedException extends BusinessException {
    public QuestionFailedException(String message) {
        super(message);
    }
}
