package com.esquinaPet.veterinariabackend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserDTO implements Serializable {

    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String lastName;
    @NotNull
    @Column(unique = true)
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$")
    @Size(max = 100)
    private String email;
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{9}$")
    @Size(min = 9, max = 9)
    private String phone;
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}-[\\dk]$")
    @Size(min = 12, max = 12)
    private String rut;
    @NotNull
    @Size(min = 6, max = 30)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$")
    private String password;
    @NotNull
    @Size(min = 6, max = 30)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$")
    private String repeatedPassword;
}
