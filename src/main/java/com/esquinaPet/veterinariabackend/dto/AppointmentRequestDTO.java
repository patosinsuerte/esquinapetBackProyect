package com.esquinaPet.veterinariabackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO implements Serializable {
    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String lastName;
    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}-[\\dk]$")
    @Size(min = 12, max = 12)
    private String rut;
    @NotNull
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$")
    @Size(max = 100)
    private String email;
    @NotNull
    @Pattern(regexp = "^[0-9]{9}$")
    @Size(min = 9, max = 9)
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime time;
    @NotNull
    private Long serviceTypeId;
    @NotNull
    private Long petId;
}
