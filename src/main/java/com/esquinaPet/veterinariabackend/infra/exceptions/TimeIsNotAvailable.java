package com.esquinaPet.veterinariabackend.infra.exceptions;


import lombok.Data;

@Data
public class TimeIsNotAvailable extends RuntimeException{

    private String code;
    private String field;


    public TimeIsNotAvailable(String message, String code, String field) {
        super(message);
        this.code = code;
        this.field = field;
    }
}
