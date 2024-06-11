package com.esquinaPet.veterinariabackend.persistence.repositories;


import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findAppointmentByDate(LocalDate date);
    List<Appointment> findAllByDate(LocalDate date);
    List<Appointment> findAllById(Long userId);
    @Query("SELECT DISTINCT a.time FROM Appointment a WHERE a.date = :date AND a.isAvailable = 'RESERVED'")
    List<LocalTime> findReservedTimesByDate(LocalDate date);

    List<LocalTime> findAvailableTimesByDate(LocalDate date);
}
