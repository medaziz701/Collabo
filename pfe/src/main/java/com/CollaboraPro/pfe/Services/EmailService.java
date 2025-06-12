package com.CollaboraPro.pfe.Services;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendEmail(SimpleMailMessage email) ;
}
