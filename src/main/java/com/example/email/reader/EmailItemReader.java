package com.example.email.reader;

import com.example.email.dto.UserDto;
import com.example.email.model.User;
import com.example.email.model.UserRole;
import com.example.email.repository.UserRepository;
import com.example.email.services.EmailServiceImpl;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class EmailItemReader implements ItemReader<List<UserDto>> {
    private Iterator<User> doctorIterator;
    private final EmailServiceImpl senderService;


    private final UserRepository userRepository;
//console
    //print item
    @Autowired
    public EmailItemReader(EmailServiceImpl senderService, UserRepository userRepository) {
        this.senderService = senderService;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> read() throws Exception {
        senderService.sendSimpleMessage("romeisaaliu1@gmail.com","Ciao","Buon Pranzo!");

        if (doctorIterator == null) {
            List<User> doctors = userRepository.findByRolesUserRole(UserRole.DOCTOR, Sort.sort(ArrayStoreException.class)); // Implement this method in UserRepository
            doctorIterator = doctors.iterator();
        }

        if (doctorIterator.hasNext()) {
            return (List<UserDto>) doctorIterator.next();

        } else {
            return null;
        }//print the doctors
    }//loggers
}
