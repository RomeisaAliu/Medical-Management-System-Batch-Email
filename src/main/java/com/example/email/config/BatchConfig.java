package com.example.email.config;

import com.example.email.service.MessageService;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.model.UserDetails;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.UserDetailsRepository;
import com.example.medicalmanagement.repository.UserRepository;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableJpaRepositories(basePackages = "com.example.medicalmanagement.repository")
@ComponentScan(basePackages = "com.example.medicalmanagement")
@EnableBatchProcessing
public class BatchConfig {

    private final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    List<UserDetails> doctorEmails = new ArrayList<>();


    @Bean
    public ItemReader<UserDetails> emailReader(UserDetailsRepository userDetailsRepository) {
        Sort sort = Sort.by(Sort.Direction.ASC, "fullName");
        this.doctorEmails = userDetailsRepository.findByRolesUserRole(UserRole.DOCTOR, sort);
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
    public Job job(Step step, JobRepository jobRepository) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public ItemProcessor<UserDto, UserDto> itemProcessor(MessageService messageService) {
        return userDto -> {
            String emailMessage = messageService.generateEmailMessage(userDto);
            messageService.sendNotification(userDto, emailMessage);
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
}