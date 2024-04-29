package com.esquinaPet.veterinariabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class LogoutResponse implements Serializable {

    private String message;

    public LogoutResponse(String message){
        this.message = message;
    }

}
