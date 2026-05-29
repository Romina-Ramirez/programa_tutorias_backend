package com.tutorias.Service.exception;

public class IdCardAlreadyExistsException extends RuntimeException {
    public IdCardAlreadyExistsException(String message) {
        super(message);
    }
}