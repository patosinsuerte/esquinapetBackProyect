package com.esquinaPet.veterinariabackend.infra.exceptions;

public class UserAlreadyExistsException extends RuntimeException{


    private String fieldName;

    public UserAlreadyExistsException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
