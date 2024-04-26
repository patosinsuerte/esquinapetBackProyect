package com.esquinaPet.veterinariabackend.shared.services;


import com.esquinaPet.veterinariabackend.persistence.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class TimesService {

    private final AppointmentRepository appointmentRepository;


    @Autowired
    public TimesService(
            AppointmentRepository appointmentRepository
    ) {
        this.appointmentRepository = appointmentRepository;
    }

    // get all Times reserved by day
    public List<LocalTime> findReservedTimesByDate(LocalDate date) {
        List<LocalTime> localTimeList = appointmentRepository.findReservedTimesByDate(date);
        if (localTimeList.isEmpty()){
            return Collections.emptyList();
        }
        return localTimeList;
    }


}
