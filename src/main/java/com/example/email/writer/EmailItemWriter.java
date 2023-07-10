
package com.example.email.writer;

import com.example.email.EmailSenderService;
import com.example.email.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailItemWriter implements ItemWriter<UserDto> {


    private final EmailSenderService senderService;
    @Autowired
    public EmailItemWriter( EmailSenderService senderService) {
        this.senderService = senderService;
    }

    @EventListener
    @Override
    public void write(Chunk<? extends UserDto> chunk) throws Exception {
        senderService.sendEmail("romeisaaliu1@gmail.com","Ciao","Buon Pranzo!");


        }
    }