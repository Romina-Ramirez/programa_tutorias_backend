package com.tutorias.Service.exception;

/** Se lanza cuando una cuenta supera el número de intentos de login permitidos. */
public class TooManyAttemptsException extends RuntimeException {

    public TooManyAttemptsException(String message) {
        super(message);
    }
}
