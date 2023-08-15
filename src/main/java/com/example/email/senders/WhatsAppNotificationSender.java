package com.example.email.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhatsAppNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(WhatsAppNotificationSender.class);


    @Override
    public void sendNotification(String doctorEmail, String message) {
        logger.info("Message sent via WhatsApp to: {}", doctorEmail);
    }
}
