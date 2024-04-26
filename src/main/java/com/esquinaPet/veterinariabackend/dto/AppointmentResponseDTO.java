package com.esquinaPet.veterinariabackend.dto;

import com.esquinaPet.veterinariabackend.domain.models.Pet;
import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO implements Serializable {

    private Long id;
    private String name;
    private String lastName;
    private String rut;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private Pet pet;
    private ServiceType serviceType;
}
