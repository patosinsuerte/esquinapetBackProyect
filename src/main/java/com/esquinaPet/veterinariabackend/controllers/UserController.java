package com.esquinaPet.veterinariabackend.controllers;


import com.esquinaPet.veterinariabackend.domain.services.auth.AuthenticationService;
import com.esquinaPet.veterinariabackend.domain.services.impl.UserServiceImpl;
import com.esquinaPet.veterinariabackend.dto.*;
import com.esquinaPet.veterinariabackend.infra.exceptions.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/users")
public class UserController {

    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(
            AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }


    /// registrar usuario
    @PostMapping("/register/user")
    public ResponseEntity<RegisteredUserDTO> registerOneUser(
            @Valid
            @RequestBody SaveUserDTO saveUserDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {


        RegisteredUserDTO registeredUserDTO = authenticationService.registerOneUser(saveUserDTO);
        System.out.println(registeredUserDTO);
        URI url = uriComponentsBuilder.path("/{id}").buildAndExpand(registeredUserDTO.getId()).toUri();
        return ResponseEntity.created(url).body(registeredUserDTO);

    }


    // REGISTRAR UN ADMIN
    @PostMapping("/register/admin")
    public ResponseEntity<RegisteredUserDTO> registerOneAdmin(
            @Valid
            @RequestBody SaveUserDTO saveUserDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        RegisteredUserDTO registeredUserDTO = authenticationService.registerOneAdmin(saveUserDTO);
        URI url = uriComponentsBuilder.path("/{id}").buildAndExpand(registeredUserDTO.getId()).toUri();
        return ResponseEntity.created(url).body(registeredUserDTO);
    }







}

