package ch.heigvd.amt.mvcProject.infrastructure.persistence.exceptions;

public class DataCorruptionException extends Exception {
    public DataCorruptionException(String msg){
        super(msg);
    }
}
