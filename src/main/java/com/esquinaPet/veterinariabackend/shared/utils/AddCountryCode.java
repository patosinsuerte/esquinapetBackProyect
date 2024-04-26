package com.esquinaPet.veterinariabackend.shared.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
public class AddCountryCode {
    public String addCountryCode(String phone){
        return "+56" + phone;
    }
}
