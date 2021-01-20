package ch.heigvd.amt.mvcProject;

public class ApiFailException extends Exception {
    public ApiFailException(String message) {
        super(message);
    }
}
