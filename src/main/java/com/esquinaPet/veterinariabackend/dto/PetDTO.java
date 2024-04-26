package com.esquinaPet.veterinariabackend.dto;

import com.esquinaPet.veterinariabackend.domain.utils.enums.PetType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO implements Serializable {

    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PetType petType;
}
