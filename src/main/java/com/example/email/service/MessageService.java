package com.example.email.service;

import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.*;
import com.example.medicalmanagement.repository.AppointmentRepository;
import com.example.medicalmanagement.repository.UserRepository;
import com.example.medicalmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final UserRepository userRepository;
    private final UserService userService;
    private SendService sendService;
    private final AppointmentRepository appointmentRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);


    @Autowired
    public MessageService(UserRepository userRepository, UserService userService, AppointmentRepository appointmentRepository, SendService sendService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
        this.sendService = sendService;
    }


    public String generateEmailMessage(UserDto userDto) {
        List<Appointment> nextAppointments = appointmentRepository.findNext24HoursAppointments(
                userDto.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24)
        );

        StringBuilder message = new StringBuilder("Hi " + userDto.getFullName() + ",\n");

        if (nextAppointments.isEmpty()) {
            message.append("You have no appointments in the next 24 hours.\n\n");
        } else {
            message.append("\nHere are your next appointments:\n");
            for (Appointment appointment : nextAppointments) {
                message.append(appointment.toString()).append("\n");

            }
        }
        message.append("\nRegards, The Best Online Medical Center");

        return message.toString();
    }


    public void sendNotification(String message) {
        Sort sort = Sort.by(Sort.Order.asc("fullName"));
        List<User> users = userRepository.findByRolesUserRole(UserRole.DOCTOR, sort);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = userService.mapToDto(user);
            userDtos.add(userDto);
        }
        for (UserDto userDto : userDtos) {
            List<NotificationType> notificationTypes = userDto.getNotificationTypes();

            if (notificationTypes != null || !notificationTypes.isEmpty()) {
                sendService.sendNotification(userDto.getEmail(), message,userDto);
                userDto.setEmailSent(true);
            } else {
                logger.warn("Unsupported notification preference for doctor: {}", userDto.getFullName());
            }

        }
    }
}