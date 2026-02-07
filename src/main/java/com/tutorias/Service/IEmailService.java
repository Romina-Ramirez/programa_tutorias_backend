package com.tutorias.Service;

import jakarta.mail.MessagingException;

public interface IEmailService {

    public void sendnewTutor(String to, String password) throws MessagingException;

    public void sendRequestRecoveryPassword(String to) throws MessagingException;
    
}
