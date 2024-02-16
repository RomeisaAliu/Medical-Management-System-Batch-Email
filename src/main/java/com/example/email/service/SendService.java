package com.example.email.service;

import com.example.email.senders.EmailNotificationSender;
import com.example.email.senders.NotificationSenderStrategy;
import com.example.email.senders.SlackNotificationSender;
import com.example.email.senders.WhatsAppNotificationSender;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.helpers.EmailContent;
import com.example.medicalmanagement.model.ContactInfo;
import com.example.medicalmanagement.model.NotificationType;

import com.example.medicalmanagement.model.User;
import com.example.sharedlibrary.service.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Setter
@Getter
@Component
public class SendService {
    private final Logger logger = LoggerFactory.getLogger(SendService.class);
    private NotificationSenderStrategy strategy;
    private final EmailNotificationSender emailNotificationSender;
    public SendService(EmailNotificationSender emailNotificationSender) {

        this.emailNotificationSender = emailNotificationSender;
    }

    public void sendNotification(ContactInfo contactInfo, String message, UserDto userDto) {

        List<NotificationType> types = userDto.getNotificationTypes();
        for (NotificationType preference : types) {
            switch (preference) {
                case EMAIL:
                    strategy = new EmailNotificationSender(emailNotificationSender.getEmailProperties());
                    strategy.sendNotification(contactInfo, message);
                    break;
                case WHATSAPP:
                    strategy = new WhatsAppNotificationSender();
                    strategy.sendNotification(contactInfo, message);
                    break;
                case SLACK:
                    strategy = new SlackNotificationSender();
                    strategy.sendNotification(contactInfo, message);
                    break;
            }

        }
    }
}