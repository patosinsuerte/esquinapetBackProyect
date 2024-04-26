package com.esquinaPet.veterinariabackend.shared.utils;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
@Data
public class AllowedTimes {

    private static final List<LocalTime> ALLOWED_TIMES = Arrays.asList(
            LocalTime.of(9, 30),
            LocalTime.of(10, 15),
            LocalTime.of(11, 0),
            LocalTime.of(11, 45),
            LocalTime.of(12, 0),
            LocalTime.of(12, 30),
            LocalTime.of(13, 15),
            LocalTime.of(15, 45),
            LocalTime.of(16, 30),
            LocalTime.of(17, 15)
    );

    // validar si un appointment request tiene la hora dentro del set
    public boolean validateTime(LocalTime time) {
        if (!ALLOWED_TIMES.contains(time)) {
            return false;
        }
        return true;
    }

}
