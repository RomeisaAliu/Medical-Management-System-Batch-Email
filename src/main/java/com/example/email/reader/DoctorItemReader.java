package com.example.email.reader;

import com.example.email.model.User;
import com.example.email.model.UserRole;
import com.example.email.repository.UserRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class DoctorItemReader implements ItemReader<List<User>> {
    private Iterator<User> doctorIterator;

    private final UserRepository userRepository;

    @Autowired
    public DoctorItemReader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> read() throws Exception {
        if (doctorIterator == null) {
            List<User> doctors = userRepository.findByRolesUserRole(UserRole.DOCTOR, Sort.sort(ArrayStoreException.class)); // Implement this method in UserRepository
            doctorIterator = doctors.iterator();
        }

        if (doctorIterator.hasNext()) {
            return (List<User>) doctorIterator.next();

        } else {
            return null;
        }
    }
}
