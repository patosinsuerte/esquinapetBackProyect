package com.esquinaPet.veterinariabackend.dto;

import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO implements Serializable {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String rut;
    private Boolean isActive;
    private String role;
    private List<AppointmentResponseDTO> appointments;
}
