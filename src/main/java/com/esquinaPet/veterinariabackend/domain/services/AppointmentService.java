package com.esquinaPet.veterinariabackend.domain.services;

import com.esquinaPet.veterinariabackend.dto.AppointmentRequestDTO;
import com.esquinaPet.veterinariabackend.dto.AppointmentResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    // get all appointments
    List<AppointmentResponseDTO> getAllAppointments();

    // GET ALL APPOINTMENTS BY ID
    List<AppointmentResponseDTO> getAllAppointmentsByUserId(Long userId);

    // get appointment by id
    AppointmentResponseDTO getAppointmentById(Long idAppointment);

    // get appointment by date
    AppointmentResponseDTO getAppointmentsByDate(LocalDate date);

    // get all appointments by date
    List<AppointmentResponseDTO> getAllAppointmentsByDate(LocalDate date);


    // Create new appointment
    AppointmentResponseDTO createNewAppointment(AppointmentRequestDTO appointmentRequestDTO);


    // Disabled one appointment
    Boolean cancelAppointmentById(Long id);

    // OBTENER CITAS DEL USUARIO AUTENTICADO
    List<AppointmentResponseDTO> findUserLoggedOwnAppointments();


    // Disabled one appointment by authenticated user
    Boolean cancelAppointmentByAuthenticatedUser(Long id);

    // OBTENER TODAS LAS CITAS ACTIVAS NO ELIMINADAS DEL USUARIO

    List<AppointmentResponseDTO> getAllUserAppointmentsActives();

}
