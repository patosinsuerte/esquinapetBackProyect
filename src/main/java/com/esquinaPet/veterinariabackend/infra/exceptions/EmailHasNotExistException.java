package com.esquinaPet.veterinariabackend.infra.exceptions;



public class EmailHasNotExistException extends RuntimeException{
    public EmailHasNotExistException(String message) {
        super(message);
    }
}
