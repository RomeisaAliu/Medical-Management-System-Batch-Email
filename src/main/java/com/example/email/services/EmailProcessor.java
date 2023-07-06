package com.example.email.services;

import com.example.email.dto.UserDto;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
@Slf4j
public class EmailProcessor implements ItemProcessor<UserDto,UserDto> {


    @Autowired
    EmailServiceImpl emailSenderService;


    @Override
    public UserDto process(UserDto userDto ) throws Exception{
      log.debug(String.valueOf(userDto.getEmail()));

      try{
        emailSenderService.sendSimpleMessage(userDto.getEmail(),"Your Appointments for the Next 24 Hours"+ "Hi"+ userDto.getFullName()+ "Here is a list of your appointments for the next 24 hours:","Regards");

//        userDto.setEmailSent(true);

      }catch (SendFailedException e) {
          log.debug(e.getMessage());
      }
        return userDto;
    }
}
