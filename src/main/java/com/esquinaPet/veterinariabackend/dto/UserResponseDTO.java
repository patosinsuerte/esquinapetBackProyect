package com.esquinaPet.veterinariabackend.dto;

import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String rut;
    private String role;
    private Boolean isActive;
    //    @JsonManagedReference
    private List<AppointmentResponseDTO> appointments;
}
