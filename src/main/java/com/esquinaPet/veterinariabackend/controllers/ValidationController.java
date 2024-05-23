package com.esquinaPet.veterinariabackend.controllers;


import com.esquinaPet.veterinariabackend.domain.services.impl.UserServiceImpl;
import com.esquinaPet.veterinariabackend.dto.ValidationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validation")
public class ValidationController {

    private final UserServiceImpl userService;

    public ValidationController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/email")
    ResponseEntity<ValidationDTO> emailValidation(
            @RequestParam
            String email
    ) {
        if (userService.emailHasExist(email)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ValidationDTO.builder()
                            .exist(true)
                            .message("El correo ya existe").build()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                ValidationDTO.builder()
                        .exist(false)
                        .message("El correo no existe").build()
        );

    }


    @GetMapping("/phone")
    ResponseEntity<ValidationDTO> phoneValidation(
            @RequestParam
            String phone
    ) {
        if (userService.phoneHasExist(phone)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ValidationDTO.builder()
                            .exist(true)
                            .message("El celular ya existe").build()
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                ValidationDTO.builder()
                        .exist(false)
                        .message("El celular no existe").build()
        );
    }


    @GetMapping("/rut")
    ResponseEntity<ValidationDTO> rutValidation(
            @RequestParam
            String rut
    ) {
        if (userService.rutHasExist(rut)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    ValidationDTO.builder()
                            .exist(true)
                            .message("El rut ya existe").build()
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                ValidationDTO.builder()
                        .exist(false)
                        .message("El rut no existe").build()
        );
    }








}
