package com.esquinaPet.veterinariabackend.controllers;

import com.esquinaPet.veterinariabackend.domain.mappers.AppointmentMapper;
import com.esquinaPet.veterinariabackend.domain.services.impl.AppointmentServiceImpl;
import com.esquinaPet.veterinariabackend.dto.AppointmentRequestDTO;
import com.esquinaPet.veterinariabackend.dto.AppointmentResponseDTO;
import com.esquinaPet.veterinariabackend.shared.services.TimesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentServiceImpl appointmentServiceImpl;
    private final TimesService timesService;

    @Autowired
    public AppointmentController(
            AppointmentServiceImpl appointmentServiceImpl,
            AppointmentMapper appointmentMapper,
            TimesService timesService
    ) {
        this.appointmentServiceImpl = appointmentServiceImpl;
        this.timesService = timesService;
    }

    //* GET APPOINTMENTS
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentServiceImpl.getAllAppointments());
    }

    //* GET APPOINTMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentServiceImpl.getAppointmentById(id));
    }

    //* GET ALL APPOINTMENTS BY DATE
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByDate(
            @PathVariable("date") String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentServiceImpl.getAllAppointmentsByDate(localDate));
    }



    //* CANCEL APPOINTMENT
    @PutMapping("/{id}/canceled")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointmentById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(appointmentServiceImpl.cancelAppointmentById(id));
    }


    //* get all times RESERVED from appointments
    @GetMapping("/reserved")
    public ResponseEntity<List<LocalTime>> findReservedTimesByDate(
            @RequestParam LocalDate date
    ) {
        List<LocalTime> reservedTimes = timesService.findReservedTimesByDate(date);
        return ResponseEntity.ok(reservedTimes);
    }





}
