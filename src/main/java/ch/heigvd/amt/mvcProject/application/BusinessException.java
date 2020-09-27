package ch.heigvd.amt.mvcProject.application;

import lombok.Value;

public class BusinessException extends Exception {
    public BusinessException(String message){
        super(message);
    }
}
