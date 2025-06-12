package com.CollaboraPro.pfe.Services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j//De Lombok, fournit un logger nommé log
@Service
public class EmailDServiceImpl implements EmailDService{

    @Autowired
    private JavaMailSender emailSender;
//to : Destinataire de l'email
    //subject : Sujet de l'email
    //text : Contenu textuel de l'email

    @Override
    public void SendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("AdminCollaboraPro@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}