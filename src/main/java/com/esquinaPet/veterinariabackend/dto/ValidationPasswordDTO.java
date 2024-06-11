package com.esquinaPet.veterinariabackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationPasswordDTO implements Serializable {

    private String passwod;
}
