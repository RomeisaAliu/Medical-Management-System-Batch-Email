package com.example.email.config;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Appointment;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.AppointmentRepository;
import com.example.medicalmanagement.repository.UserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Configuration
@EnableJpaRepositories(basePackages = "com.example.medicalmanagement.repository")
@ComponentScan(basePackages = "com.example.medicalmanagement")
@EnableBatchProcessing
public class BatchConfig {


    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;


    private final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    List<User> doctorEmails = new ArrayList<>();
    @Autowired
    private final AppointmentRepository appointmentRepository;

    public BatchConfig( AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Bean
    public ItemReader<User> emailReader(UserRepository userRepository) {
        Sort sort = Sort.by(Sort.Direction.ASC, "fullName");
        this.doctorEmails = userRepository.findByRolesUserRole(UserRole.DOCTOR,sort);
        return new IteratorItemReader<>(doctorEmails.iterator());
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                     ItemReader<UserDto> myReader, ItemProcessor<UserDto, UserDto> myProcessor,
                     ItemWriter<UserDto> myWriter) {
        return new StepBuilder("step", jobRepository)
                .<UserDto, UserDto>chunk(10, platformTransactionManager)
                .reader(myReader)
                .processor(myProcessor)
                .writer(myWriter)
                .build();
    }

    @Bean
    public Job job(Step step, JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public ItemProcessor<UserDto, UserDto> itemProcessor() {
        return userDto -> {
            logger.info("Processing item: {}", userDto.getEmail());

            List<Appointment> nextAppointments = appointmentRepository.findNext24HoursAppointments(userDto.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(24));
            String message = "Hi " + userDto.getFullName() + ",\n";

            if (nextAppointments.isEmpty()) {
                message += "You have no appointments in the next 24 hours.\n\n";
            } else {
                message += "\nHere are you next appointments:\n";
                message += "*" + appointmentRepository.findNext24HoursAppointments(userDto.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(24));
            }


            message += "\nRegards, The Best Online Medical Center";

            sendEmail(userDto.getEmail(), message);
            userDto.setEmailSent(true);
            return userDto;
        };
    }

    @Bean
    public ItemWriter<UserDto> itemWriter() {
        return items -> {
            logger.info("Sending emails...");
            logger.info("Emails sent successfully.");
        };
    }

    private void sendEmail(String email, String message) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new Authenticator() {
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
}