package com.example.email.services;

import com.example.email.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(EmailDto emailDto) {
        log.info("SendingSimpleMessage");
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("romeisaaliu1@gmail.com");
            message.setTo(emailDto.getToEmail());
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getBody());
            javaMailSender.send(message);
            log.info("Email sent successfully to: " + emailDto.getToEmail());
        } catch (MailException e) {
            log.info("Failed to send email to: " + emailDto.getToEmail());
            e.printStackTrace();
        }
    }
}
