package com.esquinaPet.veterinariabackend.shared.utils;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class AllowedDates {

    private List<LocalDate> allowedDatesList = new ArrayList<>();

    @PostConstruct
    public void generateAllowedDatesList() {
        // fecha actual
        LocalDate dateNow = LocalDate.now();
        // ano actual
        int yearNow = dateNow.getYear();
        // mes actual
        int monthNow = dateNow.getMonthValue();


        for (int i = monthNow; i <= 12; i++) {
            int firstDayOfMonth = (i == monthNow) ? dateNow.getDayOfMonth() : 1;
            int lastDayOfMonth = LocalDate.of(yearNow, monthNow, 1).lengthOfMonth();
            for (var j = firstDayOfMonth; j <= lastDayOfMonth; j++) {
                this.allowedDatesList.add(LocalDate.of(yearNow, i, j));
            }
        }
        System.out.println(this.allowedDatesList);
    }

    public boolean validateDateWithAllowedDates(LocalDate date){
       return this.allowedDatesList.contains(date);
    }
}
