package com.example.email.config;


import com.example.email.dto.EmailDto;
import com.example.email.dto.UserDto;
import com.example.email.mapper.EmailSenderMapper;
import com.example.email.processor.EmailItemProcessor;
import com.example.email.reader.EmailItemReader;
import com.example.email.services.EmailService;
import com.example.email.writer.EmailItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableJpaRepositories(basePackages = "com.example.email.repository")
@ComponentScan(basePackages = "com.example.email")
@EnableBatchProcessing
public class BatchConfig {
    private final DataSource dataSource;
    private final EmailService emailService;
    private final EmailItemWriter emailItemWriter;
    private final EmailItemProcessor emailItemProcessor;
    private final EmailItemReader emailItemReader;

    @Autowired
    public BatchConfig(DataSource dataSource, EmailService emailService, EmailItemWriter emailItemWriter, EmailItemProcessor emailItemProcessor, EmailItemReader emailItemReader) {
        this.dataSource = dataSource;
        this.emailService = emailService;
        this.emailItemWriter = emailItemWriter;
        this.emailItemProcessor = emailItemProcessor;
        this.emailItemReader = emailItemReader;
    }


    @Bean
    public JdbcCursorItemReader<UserDto> cursorItemReader() {
        log.info("Before cursorItemReader");
        JdbcCursorItemReader<UserDto> reader = new JdbcCursorItemReader<>();
        reader.setSql("SELECT u.email FROM users u JOIN user_role ur ON u.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE r.roles = 'DOCTOR'");
        reader.setDataSource(dataSource);
        reader.setFetchSize(100);
        reader.setRowMapper(new EmailSenderMapper());

        return reader;
    }


    @Bean
    public EmailItemProcessor processor() {
        log.info("Before EmailItemProcessor");
        return emailItemProcessor;
    }

    @Bean
    public EmailItemWriter writer() {
        log.info("Before EmailItemWriter");
        return emailItemWriter;

    }

    @Bean
    public EmailItemReader read() {
        log.info("Before read");
        return emailItemReader;
    }


    @Bean
    public Step emailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("Before emailStep");
        return new StepBuilder("emailStep", jobRepository)
                .<UserDto, EmailDto>chunk(10, transactionManager)
                .reader(cursorItemReader())
                .processor(new EmailItemProcessor(emailService))
                .writer(writer())
                .taskExecutor(taskExecutor())
                .startLimit(1)
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("email-sender", jobRepository)
                .flow(emailStep(jobRepository, transactionManager)).end().build();

    }


    @Bean
    public TaskExecutor taskExecutor() {
        log.info("Before taskExecutor");
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}