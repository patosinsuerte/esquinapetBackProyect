package com.esquinaPet.veterinariabackend.controllers;

import com.esquinaPet.veterinariabackend.domain.services.impl.ServiceTypeServiceImpl;
import com.esquinaPet.veterinariabackend.dto.ServiceTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service-types")
public class ServiceTypeController {

    private final ServiceTypeServiceImpl serviceTypeService;

    @Autowired
    public ServiceTypeController(ServiceTypeServiceImpl serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }


    @GetMapping
    public ResponseEntity<List<ServiceTypeDTO>> getAllServiceTypes(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceTypeService.getAllServiceTypes());
    }
}
