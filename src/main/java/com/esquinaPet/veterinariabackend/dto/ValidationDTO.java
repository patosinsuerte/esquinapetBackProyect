package com.esquinaPet.veterinariabackend.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ValidationDTO implements Serializable {
    private boolean exist;
    private String message;
}
