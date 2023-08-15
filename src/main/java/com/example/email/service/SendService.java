package com.example.email.service;

import com.example.email.helper.EmailProperties;
import com.example.email.senders.EmailNotificationSender;
import com.example.email.senders.NotificationSenderStrategy;
import com.example.email.senders.SlackNotificationSender;
import com.example.email.senders.WhatsAppNotificationSender;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.NotificationType;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Setter
@Getter
@Component
public class SendService {
    private final Logger logger = LoggerFactory.getLogger(SendService.class);
    private NotificationSenderStrategy strategy;
    private final EmailProperties emailProperties;
    public SendService(EmailProperties emailProperties) {

        this.emailProperties = emailProperties;
    }

    public void sendEmail(String email, String message) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", emailProperties.getHost());
        properties.put("mail.smtp.port", emailProperties.getPort());

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailProperties.getUsername(), emailProperties.getPassword());
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(emailProperties.getUsername()));

            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

            mimeMessage.setSubject("Appointments for " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            mimeMessage.setText(message);
            Transport.send(mimeMessage);

            logger.info("Email sent successfully to: {}", email);
        } catch (MessagingException e) {
            logger.error("Error sending email to: {}", email, e);
        }
    }

    public void sendNotification(String doctorEmail, String message, UserDto userDto) {

        List<NotificationType> types = userDto.getNotificationTypes();
        for (NotificationType preference : types) {
            switch (preference) {
                case EMAIL:
                    strategy = new EmailNotificationSender(new SendService(emailProperties));
                    strategy.sendNotification(doctorEmail, message);
                    break;
                case WHATSAPP:
                    strategy = new WhatsAppNotificationSender();
                    strategy.sendNotification(doctorEmail, message);
                    break;
                case SLACK:
                    strategy = new SlackNotificationSender();
                    strategy.sendNotification(doctorEmail, message);
                    break;
            }

        }
    }
}