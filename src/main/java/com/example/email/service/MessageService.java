package com.example.email.service;

import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Appointment;
import com.example.medicalmanagement.model.NotificationType;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
private SendService sendService;
    private final AppointmentRepository appointmentRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);


    @Autowired
    public MessageService(AppointmentRepository appointmentRepository,SendService sendService) {
        this.appointmentRepository = appointmentRepository;
        this.sendService=sendService;
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


    public void sendNotification(UserDto userDto, String message) {
        NotificationType notificationType = getNotificationType(userDto.getNotificationTypes());
        if (notificationType != null) {
            sendService.sendNotification(userDto.getEmail(), message, notificationType);
            userDto.setEmailSent(true);
            logger.info("Notification sent via {} to: {}", notificationType.name(), userDto.getEmail());
        } else {
            logger.warn("Unsupported notification preference for doctor: {}", userDto.getFullName());
        }
    }

    private NotificationType getNotificationType(List<NotificationType> notificationTypes) {
        if (notificationTypes == null || notificationTypes.isEmpty()) {
            return null;
        }
        return notificationTypes.get(0); // Assuming only one notification type is allowed per user
    }






}