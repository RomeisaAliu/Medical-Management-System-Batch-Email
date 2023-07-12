package com.example.email.processor;

import com.example.email.dto.EmailDto;
import com.example.email.dto.UserDto;
import com.example.email.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailItemProcessor implements ItemProcessor<UserDto, EmailDto> {

    private final EmailService emailService;

    @Autowired
    public EmailItemProcessor(EmailService emailService) {
        this.emailService = emailService;
    }

@Override
public EmailDto process(UserDto userDto)  {
    log.debug("Processing user: {}", userDto.getEmail());
    EmailDto emailDto = new EmailDto();
    emailDto.setToEmail(userDto.getEmail());
    emailDto.setSubject("Ferie");
    emailDto.setBody("I have in 4 August day holiday");
    emailService.sendSimpleMessage(emailDto);
    log.debug("Email sent to '{}': Subject - '{}', Content - '{}'", emailDto.getToEmail(), emailDto.getSubject(), emailDto.getBody());
    return emailDto;
}
}

