package com.example.email.reader;//package com.example.email.reader;//package com.example.email.reader;

import com.example.email.dto.UserDto;
import com.example.email.model.User;
import com.example.email.model.UserRole;
import com.example.email.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class EmailItemReader implements ItemReader<UserDto> {
    private Iterator<UserDto> doctorIterator;
    private final UserRepository userRepository;

    //console
    //print item
    @Autowired
    public EmailItemReader(UserRepository userRepository) {
        log.info("--- Email Reader");

        this.userRepository = userRepository;
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            log.info(users.get(i).getEmail());
        }
    }

    @Override
    public UserDto read() {
        log.info("--- Email Reader second");

        if (doctorIterator == null) {
            List<UserDto> doctors = userRepository.findByRolesUserRole(UserRole.DOCTOR); // Implement this method in UserRepository
            doctorIterator = doctors.iterator();

            log.info("--- Email Reader third");
        }

        if (doctorIterator.hasNext()) {
            log.info("Hello");
            return doctorIterator.next();


        } else {
            log.info("Joo");
            return null;
        }
    }

}