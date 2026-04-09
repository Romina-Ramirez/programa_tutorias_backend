package com.tutorias.Service;

import jakarta.mail.MessagingException;

public interface IEmailService {

    public void sendnewTutorOrAdmin(String to, String password, String role) throws MessagingException;

    public void sendRequestRecoveryPassword(String to) throws MessagingException;
    
}
