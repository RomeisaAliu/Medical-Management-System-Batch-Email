package com.example.email.Senders;

import com.example.email.service.MessageService;
import com.example.medicalmanagement.model.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhatsAppNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(WhatsAppNotificationSender.class);


    @Override
    public void sendNotification(String doctorEmail, String message, NotificationType type) {
        logger.info("Message sent via WhatsApp to: {}", doctorEmail);

    }
}
