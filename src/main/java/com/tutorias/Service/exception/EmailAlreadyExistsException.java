package com.tutorias.Service.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
    
}
