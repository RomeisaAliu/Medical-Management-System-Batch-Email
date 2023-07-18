package com.example.email.writer;

import com.example.email.config.BatchConfig;
import com.example.medicalmanagement.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class myWriter implements ItemWriter<UserDto> {
    private final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Override
    public void write(Chunk<? extends UserDto> chunk) throws Exception {
        logger.info("Sending emails...");
        logger.info("Emails sent successfully.");
    }
}
