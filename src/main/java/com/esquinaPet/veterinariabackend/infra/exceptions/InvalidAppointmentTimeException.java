package com.esquinaPet.veterinariabackend.infra.exceptions;

public class InvalidAppointmentTimeException extends RuntimeException{


    public InvalidAppointmentTimeException(String message) {
        super(message);
    }


}
