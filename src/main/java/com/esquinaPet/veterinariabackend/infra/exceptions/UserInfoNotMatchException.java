package com.esquinaPet.veterinariabackend.infra.exceptions;


import lombok.Data;

@Data
public class UserInfoNotMatchException extends RuntimeException{

    private String message;
    private String field;
    private String valueInRequest;
    private String valueInProfile;

    public UserInfoNotMatchException(String message, String field, String valueInRequest, String valueInProfile) {
        super(message);
        this.field = field;
        this.valueInRequest = valueInRequest;
        this.valueInProfile = valueInProfile;
    }

}
