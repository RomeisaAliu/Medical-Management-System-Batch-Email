package com.example.email.writer;

import com.example.email.dto.UserDto;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class SendEmailWriter implements ItemWriter<UserDto> {


    @Override
    public void write(Chunk<? extends UserDto> chunk) throws Exception {

    }
}
