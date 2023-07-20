package com.example.email.writer;

import com.example.medicalmanagement.dto.UserDto;
import org.slf4j.Logger;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class Writer implements ItemWriter<UserDto> {

    private final Logger logger ;
    public Writer(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void write(Chunk<? extends UserDto> chunk){
        logger.info("Sending emails...");
        logger.info("Emails sent successfully.");
    }

}