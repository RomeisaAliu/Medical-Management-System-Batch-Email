package com.example.email.services;

import jakarta.mail.SendFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl  {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to,String subject,String text) throws SendFailedException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("romeisaaliu1@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }
}
