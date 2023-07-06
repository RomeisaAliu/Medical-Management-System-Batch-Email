package com.example.email.repository;

import com.example.email.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByDoctorIdAndAppointmentDateStartTimeBeforeAndAppointmentDateEndTimeAfter(Long  doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT a FROM Appointment a WHERE (a.patient.Id= :patientId OR a.doctor.Id = :doctorId) AND" +
            "((a.appointmentDateStartTime < :endDateTime AND a.appointmentDateEndTime > :startDateTime) OR " +
            "(a.appointmentDateStartTime >= :startDateTime AND a.appointmentDateStartTime < :endDateTime) OR " +
            "(a.appointmentDateEndTime > :startDateTime AND a.appointmentDateEndTime <= :endDateTime))")
    List<Appointment> findConflictingAppointments(@Param("patientId") Long patientId,
                                                  @Param("doctorId") Long doctorId,
                                                  @Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);




        Appointment save(Appointment appointment);

    List<Appointment> findByDoctorId(Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.Id = :doctorId AND a.appointmentDateStartTime >= :currentDateTime AND a.appointmentDateStartTime < :next24HoursDateTime")
    List<Appointment> findNext24HoursAppointments(@Param("doctorId") Long doctorId, @Param("currentDateTime") LocalDateTime currentDateTime, @Param("next24HoursDateTime") LocalDateTime next24HoursDateTime);


}