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
public class WhatsAppNotificationSender implements NotificationSenderStrategy {
    private final Logger logger = LoggerFactory.getLogger(WhatsAppNotificationSender.class);


    @Override
    public void sendNotification(ContactInfo contactInfo, String message) {
        logger.info("*****Message sent via WhatsApp to: {}", contactInfo.getPhoneNumber());
    }
}
