
package com.example.email.writer;

import com.example.email.dto.EmailDto;
import com.example.email.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailItemWriter implements ItemWriter<EmailDto> {

    private final EmailService emailService;

    @Autowired
    public EmailItemWriter(EmailService emailService) {
        log.info("--- Before Email Writer");
        this.emailService = emailService;

    }

    @Override
    public void write(Chunk<? extends EmailDto> emailDtos) {
        log.info("Before Write");
        for (EmailDto emailDto : emailDtos) {
            emailService.sendSimpleMessage(emailDto);
            log.debug("Email sent to '{}': Subject - '{}', Content - '{}'", emailDto.getToEmail(), emailDto.getSubject(), emailDto.getBody());
        }
    }
}