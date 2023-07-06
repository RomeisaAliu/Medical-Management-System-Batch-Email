package com.example.email.processor;

import com.example.email.dto.UserDto;
import com.example.email.services.EmailServiceImpl;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class EmailItemProcessor implements ItemProcessor<UserDto, UserDto> {


    EmailServiceImpl emailService;

    @Override
    public UserDto process(UserDto userDto) throws Exception {
        log.debug("processor: {}", userDto);
        try {
            emailService.sendSimpleMessage(userDto.getEmail(), "Your Order has been shipped!", "Thank you for shopping with us");
            userDto.setEmailSent(true);
        } catch (SendFailedException sendFailedException) {
            log.debug("error: {}", sendFailedException.getMessage());
        }
        return userDto;
    }
}