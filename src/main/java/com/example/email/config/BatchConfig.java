package com.example.email.config;

import com.example.email.dto.UserDto;
import com.example.email.mapper.EmailSenderMapper;
import com.example.email.processor.EmailItemProcessor;
import com.example.email.writer.SendEmailWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Random;

@EnableBatchProcessing
@Slf4j
@Configuration
public class BatchConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private DataSource dataSource;

    private final String JOB_NAME = "emailSenderJob";
    private final String STEP_NAME = "emailSenderStep";



    Random random = new Random();
    int randomWithNextInt = random.nextInt();




    @Bean
    public Job emailSenderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME+randomWithNextInt,jobRepository)
                .start(emailSenderStep(jobRepository,transactionManager))
                .build();
    }
    @Bean
    public Step emailSenderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new
                StepBuilder(STEP_NAME,jobRepository)
                .<UserDto, UserDto>chunk(100,transactionManager)
                .reader(emailReader())
                .processor(sendEmailItemProcessor())
                .writer(emailWriter())
                .build();
    }

    @Bean
    public ItemWriter<UserDto> userDtoItemWriter(){
        return new SendEmailWriter();
    }
    @Bean
    public ItemProcessor<UserDto, UserDto> sendEmailItemProcessor() {
        return new EmailItemProcessor();
    }

    @Bean
    public ItemWriter<UserDto> emailWriter() {
        return new SendEmailWriter();
    }

    @Bean
    public ItemReader<UserDto> emailReader() {
        String sql = "SELECT email FROM speciality";
        return new JdbcCursorItemReaderBuilder<UserDto>()
                .name("emailReader")
                .sql(sql)
                .dataSource(dataSource)
                .rowMapper(new EmailSenderMapper())
                .build();
    }
}