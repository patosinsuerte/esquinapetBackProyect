package com.esquinaPet.veterinariabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<Map<String, String>> test(){
        Map<String, String> response = new HashMap<>();
        response.put("message", "Esta es una prueba de autenticacion");
        return ResponseEntity.ok(response);
    }

}
