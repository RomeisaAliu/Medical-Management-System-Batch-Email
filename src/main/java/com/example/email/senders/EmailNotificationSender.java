package com.example.email.senders;

import com.example.email.service.SendService;


import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class EmailNotificationSender implements NotificationSenderStrategy {

    private final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);
    private final SendService sendService;

    public EmailNotificationSender(SendService sendService) {

        this.sendService = sendService;
    }


    @Override
    public void sendNotification(String doctorEmail, String message) {
        logger.error("Message sent via EMAIL to {}", doctorEmail);
        sendService.sendEmail(doctorEmail, message);
    }

}
