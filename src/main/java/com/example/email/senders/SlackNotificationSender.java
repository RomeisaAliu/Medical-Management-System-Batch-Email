package com.example.email.senders;

import com.example.medicalmanagement.model.ContactInfo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
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
