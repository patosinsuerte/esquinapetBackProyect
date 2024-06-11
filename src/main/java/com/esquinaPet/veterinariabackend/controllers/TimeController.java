package com.esquinaPet.veterinariabackend.controllers;


import com.esquinaPet.veterinariabackend.shared.services.TimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimesService timesService;

    @Autowired
    public TimeController(
            TimesService timesService
    ) {
        this.timesService = timesService;
    }


    @GetMapping("/reserved/by/date")
    public ResponseEntity<List<LocalTime>> getTimesReservedByDate(
            @RequestParam("date")  LocalDate date
    ) {
        return ResponseEntity.ok(this.timesService.findReservedTimesByDate(date));
    }
}
