package ch.heigvd.amt.mvcProject.application.answer;

import ch.heigvd.amt.mvcProject.application.BusinessException;

public class AnswerFailedException extends BusinessException {
    public AnswerFailedException(String message) {
        super(message);
    }
}
