package com.example.email.reader;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.*;
import com.example.medicalmanagement.repository.UserDetailsRepository;
import com.example.medicalmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
@Component
public class Reader implements ItemReader<UserDto> {
    private final UserDetailsRepository userRepository;
    private Iterator<UserDto> userDtoIterator;
    public Reader(UserDetailsRepository userDetailsRepository) {
        this.userRepository = userDetailsRepository;
        Logger logger = LoggerFactory.getLogger(Reader.class);
        logger.info("reading...");
    }
    @Override
    public UserDto read() {
        if (userDtoIterator == null) {
            Sort sort = Sort.by(Sort.Direction.ASC, "fullName");
            List<UserDetails> doctors = userRepository.findByRolesUserRole(UserRole.DOCTOR, sort);
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
    private UserDto convertToDto(UserDetails doctor) {
        UserDto userDto = new UserDto();
        userDto.setId(doctor.getId());
        userDto.setContactInfo(doctor.getContactInfo());
        userDto.setFullName(doctor.getFullName());
        userDto.setRoles(doctor.getRoles().stream()
                .map(Role::getUserRole)
                .toList());
        userDto.setSpecialities(doctor.getSpecialities().stream()
                .map(Speciality::getName)
                .toList());
        userDto.setNotificationTypes(doctor.getNotificationTypes().stream().map(UserNotificationType::getNotificationType).toList());
        userDto.setContactInfo(doctor.getContactInfo());
        return userDto;
    }
}