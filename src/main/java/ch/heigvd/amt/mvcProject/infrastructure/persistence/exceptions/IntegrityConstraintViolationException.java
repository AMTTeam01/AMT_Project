package ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions;

public class IntegrityConstraintViolationException extends Exception {
    public IntegrityConstraintViolationException(String msg){
        super(msg);
    }
}
