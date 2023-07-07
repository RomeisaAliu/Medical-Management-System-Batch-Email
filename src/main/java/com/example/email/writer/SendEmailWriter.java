package com.example.email.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendEmailWriter implements ItemWriter<MimeMessagePreparator> {


private JavaMailSender mailSender;

public SendEmailWriter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        }


@Override
public void write(Chunk<? extends MimeMessagePreparator> chunk) throws Exception {
        for (MimeMessagePreparator preparator : chunk) {
        mailSender.send(preparator);
        }
    }
}

