package com.esquinaPet.veterinariabackend.controllers;

import com.esquinaPet.veterinariabackend.domain.services.auth.AuthenticationService;
import com.esquinaPet.veterinariabackend.domain.services.impl.AppointmentServiceImpl;
import com.esquinaPet.veterinariabackend.dto.*;
import com.esquinaPet.veterinariabackend.infra.exceptions.EmailHasNotExistException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AppointmentServiceImpl appointmentService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AppointmentServiceImpl appointmentService, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.appointmentService = appointmentService;
    }


    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate(
            @RequestParam String jwt
    ) {
        boolean isTokenValid = authenticationService.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @Valid
            @RequestBody
            AuthenticationRequestDTO authenticationRequest
    ) {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(authenticationResponseDTO);
    }



    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> findMyProfile() {
        UserProfileDTO user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok(user);
    }


    // LOGOUT METHOD

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            HttpServletRequest httpServletRequest
    ){
        authenticationService.logout(httpServletRequest);
        return ResponseEntity.ok(new LogoutResponse("Logout exitoso"));
    }


/*********************LOGGED USER****************************************************/

    //* CREATE NEW APPOINTMENT
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDTO> createOneAppointment(
            @Valid
            @RequestBody
            AppointmentRequestDTO appointmentRequestDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {

        AppointmentResponseDTO appointmentResponseDTO = appointmentService.createNewAppointment(appointmentRequestDTO);
        URI uri = uriComponentsBuilder.path("/{id}").buildAndExpand(appointmentResponseDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(appointmentResponseDTO);
    }

    // GET ALL APPIOINTMENTS BY LOGGED ID USER
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointmentsByLoggedIdUser() {
        return ResponseEntity.ok(appointmentService.findUserLoggedOwnAppointments());
    }

    // cancel appoinment by authenticated user
    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<Boolean> cancelAppointmentByIdAuthenticatedUser(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.appointmentService.cancelAppointmentByAuthenticatedUser(id));
    }


    // edit user info
    @PatchMapping("/user/edit")
    public ResponseEntity<UserResponseDTO> editLoggedUserInfo(
            @Valid
            @RequestBody EditUserRequestDTO editUserRequestDTO
    ){
        return ResponseEntity.ok(this.authenticationService.editLoggedUserInfo(editUserRequestDTO));
    }



    // TODOS LOS APPOINTMENTS DEL USUARIO ACTIVAS
    @GetMapping("/appointments/actives")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllUserActiveAppointments(){
        return ResponseEntity.ok(this.appointmentService.getAllUserAppointmentsActives());
    }
}
