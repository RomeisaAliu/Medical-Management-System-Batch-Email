package com.example.email.senders;

import com.example.medicalmanagement.model.ContactInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(SlackNotificationSender.class);

    @Override
    public void sendNotification(ContactInfo recipientContactInfo, String message) {
        if (recipientContactInfo != null) {
            String slackUserName = recipientContactInfo.getSlackUserName();
            if (slackUserName != null) {
                logger.info("*****Message sent via Slack to: {}", slackUserName);
            } else {
                logger.info("*****Message sent via Slack to {} (No Slack username)", recipientContactInfo);
            }
        } else {
            logger.error("*****recipientContactInfo is null");
        }
    }
}
