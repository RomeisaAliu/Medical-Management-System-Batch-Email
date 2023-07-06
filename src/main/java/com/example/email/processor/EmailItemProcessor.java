package com.example.email.processor;

import com.example.email.model.User;
import com.example.email.services.EmailServiceImpl;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailItemProcessor implements ItemProcessor<User, User> {

    private final EmailServiceImpl emailSenderService;

    @Autowired
    public EmailItemProcessor(EmailServiceImpl emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Override
    public User process(User user) throws Exception {
        log.debug("Processing user: {}", user.getEmail());

        try {
            String subject = "Your Appointments for the Next 24 Hours";
            String content = "Hi " + user.getFullName() + ",\n\n" +
                    "Here is a list of your appointments for the next 24 hours:\n\n" +
                    "...\n\n" + // Add the list of appointments here
                    "Regards";

            emailSenderService.sendSimpleMessage(user.getEmail(), subject, content);
            log.debug("Email sent to '{}': Subject - '{}', Content - '{}'", user.getEmail(), subject, content);

            user.setEmailSent(true);

        } catch (SendFailedException e) {
            log.debug("Failed to send email to '{}': {}", user.getEmail(), e.getMessage());
        }

        return user;
    }
}
