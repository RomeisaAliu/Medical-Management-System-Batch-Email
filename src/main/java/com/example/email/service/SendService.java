package com.example.email.service;

import com.example.email.senders.EmailNotificationSender;
import com.example.email.senders.NotificationSenderStrategy;
import com.example.email.senders.SlackNotificationSender;
import com.example.email.senders.WhatsAppNotificationSender;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.NotificationType;
import com.example.medicalmanagement.repository.UserRepository;
import com.example.medicalmanagement.service.UserService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SendService {
    private final Logger logger = LoggerFactory.getLogger(SendService.class);
    private NotificationSenderStrategy strategy;
    private UserDto userDto;
    private final UserRepository userRepository;
    private final UserService userService;


//    private Map<NotificationType, NotificationSenderStrategy> notificationSenders;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;

    public SendService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void sendEmail(String email, String message) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail. smtp.port", port);

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
    public void sendNotification(String doctorEmail, String message, UserDto userDto) {

        List<NotificationType> types = userDto.getNotificationTypes();
        for (NotificationType preference : types) {
            switch (preference) {
//                case EMAIL:
//                    strategy = new EmailNotificationSender(new SendService(userRepository,userService));
//                    strategy = new EmailNotificationSender("sildiricku3@gmail.com","tgimiemtglpvgecy","smtp.gmail.com",587);

//                    strategy.sendNotification(doctorEmail, message);
//                    sendEmail(doctorEmail, message);
                case WHATSAPP:
                    strategy = new WhatsAppNotificationSender();
                    strategy.sendNotification(doctorEmail, message);
                case SLACK:
                    strategy = new SlackNotificationSender();
                    strategy.sendNotification(doctorEmail, message);
            }
        }
    }
}