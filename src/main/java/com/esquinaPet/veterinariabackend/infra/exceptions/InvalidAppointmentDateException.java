package com.esquinaPet.veterinariabackend.infra.exceptions;

public class InvalidAppointmentDateException extends RuntimeException{

    public InvalidAppointmentDateException(String message) {
        super(message);
    }
}
