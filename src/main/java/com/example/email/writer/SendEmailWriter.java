package com.example.email.writer;

import com.example.email.model.Appointment;
import com.example.email.model.User;
import com.example.email.repository.AppointmentRepository;
import com.example.email.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SendEmailWriter implements ItemWriter<List<Appointment>> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    public final AppointmentRepository appointmentRepository;

    @Autowired
    public SendEmailWriter(JavaMailSender javaMailSender, UserRepository userRepository,
                           AppointmentRepository appointmentRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void write(Chunk<? extends List<Appointment>> chunk) throws Exception {

        for (List<Appointment> appointments : chunk) {
            if (!appointments.isEmpty()) {
                Appointment appointment = appointments.get(0);
                User doctor = appointment.getDoctor();
                String doctorEmail = userRepository.findDoctorEmailById(doctor.getId()); // Retrieve doctor's email from the database

                if (doctorEmail != null) {
                    String subject = "Appointments for " + dateFormat.format(new Date());
                    StringBuilder content = new StringBuilder();
                    content.append("Hi ").append(doctor.getFullName()).append(",\n\n");
                    content.append("Here are your next appointments:\n\n");

                    for (Appointment appt : appointments) {
                        String appointmentTime = timeFormat.format(appt.getAppointmentDateStartTime());
                        String patientFullName = appt.getPatient().getFullName();
                        content.append(appointmentTime).append(" ").append(dateFormat.format(appt.getAppointmentDateStartTime()))
                                .append(" - ").append(patientFullName).append("\n");
                    }

                    content.append("\nRegards,\nBest Medical Center");

                    sendEmail(doctorEmail, subject, content.toString());
                }
            }
        }
    }

    private void sendEmail(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);

        log.info("Email sent to '{}': Subject - '{}', Content - '{}'", recipient, subject, content);
    }


}
