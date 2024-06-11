package com.esquinaPet.veterinariabackend.shared.utils;


import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
@Service
public class ValidateBeforeDates {

    public  boolean verifyIfDateIsBeforeToActual(LocalDate date){
        if (!date.isBefore(LocalDate.now())){
            return true;
        }
        return false;
    }



}
