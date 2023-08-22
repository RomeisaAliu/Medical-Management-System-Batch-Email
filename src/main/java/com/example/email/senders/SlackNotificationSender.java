package com.example.email.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(SlackNotificationSender.class);

    @Override
    public void sendNotification(String recipientContactInfo, String message) {
        logger.info("Message sent via Slack to: {}", recipientContactInfo);
    }
}
