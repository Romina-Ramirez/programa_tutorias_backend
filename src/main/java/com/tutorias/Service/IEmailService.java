package com.tutorias.Service;

import jakarta.mail.MessagingException;

public interface IEmailService {

    public void sendAccountActivation(String to, String activationToken) throws MessagingException;

    public void sendRequestRecoveryPassword(String to, String resetToken) throws MessagingException;
    
}
