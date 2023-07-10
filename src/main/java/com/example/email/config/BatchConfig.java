package com.example.email.config;


import com.example.email.EmailSenderService;
import com.example.email.dto.UserDto;
import com.example.email.mapper.EmailSenderMapper;
import com.example.email.processor.EmailItemProcessor;
import com.example.email.services.EmailServiceImpl;
import com.example.email.writer.EmailItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.email.repository")
@ComponentScan(basePackages = "com.example.email")
public class BatchConfig {
    private final DataSource dataSource;
    private final EmailServiceImpl emailService;
    private final EmailSenderService senderService;

    @Autowired
    public BatchConfig(DataSource dataSource, EmailServiceImpl emailService, EmailSenderService senderService) {
        this.dataSource = dataSource;

        this.emailService = emailService;
        this.senderService = senderService;
    }


    @Bean
    public JdbcCursorItemReader<UserDto> cursorItemReader(){
        JdbcCursorItemReader<UserDto> reader = new JdbcCursorItemReader<>();
        reader.setSql("SELECT email FROM medical_management_system.speciality;");
        reader.setDataSource(dataSource);
        reader.setFetchSize(100);
        reader.setRowMapper(new EmailSenderMapper());

        return reader;
    }

    @Bean
    public JdbcPagingItemReader<UserDto> pagingItemReader(){
        JdbcPagingItemReader<UserDto> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10);
        reader.setRowMapper(new EmailSenderMapper());

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("select id, email, fullName, phoneNumber");
        queryProvider.setFromClause("from users");
        queryProvider.setSortKeys(sortKeys);


        reader.setQueryProvider(queryProvider);

        return reader;
    }

    @Bean
    public EmailItemProcessor processor() {
        return new EmailItemProcessor(emailService);
    }

    @Bean
    public EmailItemWriter writer() {
        return  new EmailItemWriter( senderService);

    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("email",jobRepository).
                <UserDto,UserDto>chunk(10,transactionManager)
                .reader(cursorItemReader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
        return new JobBuilder("email-sender",jobRepository)
                .flow(step1(jobRepository,transactionManager)).end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}