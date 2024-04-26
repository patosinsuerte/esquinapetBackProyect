package com.esquinaPet.veterinariabackend.dto;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeDTO implements Serializable {
    @NonNull
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private String serviceName;
}
