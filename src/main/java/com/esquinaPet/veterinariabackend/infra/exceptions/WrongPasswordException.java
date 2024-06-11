package com.esquinaPet.veterinariabackend.infra.exceptions;

public class WrongPasswordException extends RuntimeException{

    public WrongPasswordException(String message) {
        super(message);
    }
}
