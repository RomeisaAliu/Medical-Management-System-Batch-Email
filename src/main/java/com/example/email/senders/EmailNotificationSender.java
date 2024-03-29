package com.example.email.senders;



import com.example.email.helper.EmailProperties;
import com.example.medicalmanagement.model.ContactInfo;
import com.example.medicalmanagement.model.UserDetails;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Getter
@Setter
@Component
public class EmailNotificationSender implements NotificationSenderStrategy {

    private final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);
    private EmailProperties emailProperties;

    public EmailNotificationSender(EmailProperties emailProperties) {

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



    @Override
    public void sendNotification(ContactInfo recipientContactInfo, String message) {
        if (recipientContactInfo != null && recipientContactInfo.getUser() != null && recipientContactInfo.getUser().getUser().getEmail() != null) {
            String userEmail = recipientContactInfo.getUser().getUser().getEmail();
            logger.info("*****Message sent via EMAIL to {}", userEmail);
            sendEmail(userEmail, message);
        } else {
            logger.info("*****recipientContactInfo is null or associated UserDetails is null or UserDetails's associated User's email is null");
        }
    }

}
