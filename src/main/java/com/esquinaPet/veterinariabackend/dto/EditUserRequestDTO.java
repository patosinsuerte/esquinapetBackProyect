package com.esquinaPet.veterinariabackend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class EditUserRequestDTO implements Serializable {
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String name;
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String lastName;
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{9}$")
    private String phone;
}