package com.example.email.service;

import com.example.email.Senders.EmailNotificationSender;
import com.example.email.Senders.NotificationSenderStrategy;
import com.example.email.Senders.SlackNotificationSender;
import com.example.email.Senders.WhatsAppNotificationSender;
import com.example.medicalmanagement.model.NotificationType;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@Service
public class SendService {

    private final Logger logger = LoggerFactory.getLogger(SendService.class);

    private  Map<NotificationType, NotificationSenderStrategy> notificationSenders;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;


    public void sendEmail(String email, String message) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(username));

            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

            mimeMessage.setSubject("Appointments for " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            mimeMessage.setText(message);
            Transport.send(mimeMessage);

            logger.info("Email sent successfully to: {}", email);
        } catch (MessagingException e) {
            logger.error("Error sending email to: {}", email, e);
        }
    }


    public SendService() {
        notificationSenders = new HashMap<>();
        notificationSenders.put(NotificationType.EMAIL, new EmailNotificationSender(this));
        notificationSenders.put(NotificationType.WHATSAPP, new WhatsAppNotificationSender());
        notificationSenders.put(NotificationType.SLACK, new SlackNotificationSender());

    }
    public void sendNotification(String doctorEmail, String message, NotificationType type) {
        NotificationSenderStrategy sender = notificationSenders.get(type);
        if (sender != null) {
            logger.info("Notification sent to doctor: {} via {}", doctorEmail, type.name().toLowerCase());
            sender.sendNotification(doctorEmail, message, type);
        } else {
            logger.warn("Unsupported notification type: {}", type);
        }
    }

}