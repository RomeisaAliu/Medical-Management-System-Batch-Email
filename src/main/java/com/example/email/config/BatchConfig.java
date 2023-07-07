package com.example.email.config;

import com.example.email.mapper.EmailSenderMapper;
import com.example.email.model.Appointment;
import com.example.email.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@EnableBatchProcessing
@Slf4j
@Configuration
public class BatchConfig {

    @Value("${file.path}")
    private String filePath;

    private final DataSource dataSource;
    @Autowired
    public BatchConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public FlatFileItemReader<User> reader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setName("Reader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    private LineMapper<User> lineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","email", "fullName", "phoneNumber");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public ItemProcessor<User, List<Appointment>> sendEmailItemProcessor() {
        return sendEmailItemProcessor();
    }

    @Bean
    public ItemWriter<List<Appointment>> SendEmailWriter() {

        return SendEmailWriter();
    }

    @Bean
    public ItemReader<User> emailReader() {
        String sql = "SELECT email FROM users";
        return new JdbcCursorItemReaderBuilder<User>()
                .name("emailReader")
                .sql(sql)
                .dataSource(dataSource)
                .rowMapper(new EmailSenderMapper())
                .build();
    }





    @Bean
    public Job emailSenderJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("emailSenderJob", jobRepository)
                .start(emailSenderStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step emailSenderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new
                StepBuilder("emailSenderStep", jobRepository)
                .<User, List<Appointment>>chunk(100, transactionManager)
                .reader(emailReader())
                .processor(sendEmailItemProcessor())
                .reader(emailReader())
                .writer(SendEmailWriter())
                .build();
    }



    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}