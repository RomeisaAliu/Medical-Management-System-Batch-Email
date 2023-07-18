package com.example.email.reader;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Role;
import com.example.medicalmanagement.model.Speciality;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class myReader implements ItemReader<UserDto> {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(myReader.class);
    private Iterator<UserDto> userDtoIterator;

    public myReader(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("reading...");
    }


    @Override
    public UserDto read() {
        if (userDtoIterator == null) {
            List<User> doctors = userRepository.findDoctors();
            List<UserDto> doctorDtos = doctors.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            userDtoIterator = doctorDtos.iterator();
        }

        if (userDtoIterator.hasNext()) {
            return userDtoIterator.next();
        } else {
            return null;
        }
    }

    private UserDto convertToDto(User doctor) {
        UserDto userDto = new UserDto();
        userDto.setId(doctor.getId());
        userDto.setEmail(doctor.getEmail());
        userDto.setFullName(doctor.getFullName());
        userDto.setRoles(doctor.getRoles().stream()
                .map(Role::getUserRole)
                .collect(Collectors.toList()));
        userDto.setSpecialities(doctor.getSpecialities().stream()
                .map(Speciality::getName)
                .collect(Collectors.toList()));
        return userDto;
    }
}