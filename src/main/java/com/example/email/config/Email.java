package com.example.email.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class Email {
    private String to;
    private String subject;
    private String body;

}
