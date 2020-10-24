package ch.heigvd.amt.mvcProject.application.comment;

import ch.heigvd.amt.mvcProject.application.BusinessException;

public class CommentFailedException  extends BusinessException {
    public CommentFailedException(String message) {
        super(message);
    }
}
