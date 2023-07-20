package com.example.email.service;

import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Appointment;
import com.example.medicalmanagement.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public MessageService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
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
}