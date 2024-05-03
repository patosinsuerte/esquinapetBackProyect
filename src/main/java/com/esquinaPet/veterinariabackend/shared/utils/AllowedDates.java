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

        // Reiniciar la lista de fechas permitidas
        this.allowedDatesList.clear();
        for (int i = monthNow; i <= 12; i++) {
            // Obtener el último día del mes
            int lastDayOfMonth = LocalDate.of(yearNow, i, 1).lengthOfMonth();
            for (int j = 1; j <= lastDayOfMonth; j++) {
                this.allowedDatesList.add(LocalDate.of(yearNow, i, j));
            }
        }
    }

    public boolean validateDateWithAllowedDates(LocalDate date){
       return this.allowedDatesList.contains(date);
    }
}
