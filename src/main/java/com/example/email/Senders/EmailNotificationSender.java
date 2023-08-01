package com.example.email.Senders;

import com.example.email.service.SendService;
import com.example.medicalmanagement.model.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);

    private final SendService sendService;

    public EmailNotificationSender(SendService sendService) {
        this.sendService = sendService;
    }


    @Override
    public void sendNotification(String doctorEmail, String message, NotificationType type) {
        sendService.sendEmail(doctorEmail, message);
        logger.info("Message sent via Email to: {}", doctorEmail);
    }
}
