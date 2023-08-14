package com.example.email.senders;

import com.example.email.service.SendService;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class EmailNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);
    private  SendService sendService;

    public EmailNotificationSender(SendService sendService) {
        this.sendService=sendService;
    }

//    @Value("${spring.mail.username}")
//    private String username;
//    @Value("${spring.mail.password}")
//    private String password;
//    @Value("${spring.mail.host}")
//    private String host;
//    @Value("${spring.mail.port}")
//    private int port;


    @Override
    public void sendNotification(String doctorEmail, String message) {
        sendService.sendEmail(doctorEmail, message);
    }

}
