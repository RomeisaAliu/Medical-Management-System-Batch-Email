package com.example.email.Senders;

import com.example.medicalmanagement.model.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(SlackNotificationSender.class);

    @Override
    public void sendNotification(String doctorEmail, String message, NotificationType type) {
        logger.info("Message sent via Slack to: {}", doctorEmail);

    }
}
