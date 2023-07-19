package com.example.email.reader;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Role;
import com.example.medicalmanagement.model.Speciality;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

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
            Sort sort = Sort.by(Sort.Direction.ASC, "fullName");
            List<User> doctors = userRepository.findByRolesUserRole(UserRole.DOCTOR,sort);
            List<UserDto> doctorDtos = doctors.stream()
                    .map(this::convertToDto)
                    .toList();
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
                .toList());
        userDto.setSpecialities(doctor.getSpecialities().stream()
                .map(Speciality::getName)
                .toList());
        return userDto;
    }
}