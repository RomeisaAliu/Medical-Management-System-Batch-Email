package com.example.email.writer;

import com.example.email.config.BatchConfig;
import com.example.medicalmanagement.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class myWriter implements ItemWriter<UserDto> {
    private Logger logger = LoggerFactory.getLogger(myWriter.class);

    public myWriter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void write(Chunk<? extends UserDto> chunk) throws Exception {
        logger.info("Sending emails...");
        logger.info("Emails sent successfully.");
    }
}
