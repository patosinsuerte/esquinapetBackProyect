package com.esquinaPet.veterinariabackend.domain.services.auth;

import com.esquinaPet.veterinariabackend.domain.mappers.RegisteredUserMapper;
import com.esquinaPet.veterinariabackend.domain.mappers.UserProfileMapper;
import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.domain.services.impl.UserServiceImpl;
import com.esquinaPet.veterinariabackend.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserServiceImpl userService;
    private final RegisteredUserMapper registeredUserMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserProfileMapper userProfileMapper;

    @Autowired
    public AuthenticationService(
            UserServiceImpl userService,
            RegisteredUserMapper registeredUserMapper,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserProfileMapper userProfileMapper
    ) {
        this.userService = userService;
        this.registeredUserMapper = registeredUserMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userProfileMapper = userProfileMapper;
    }


    // register user
    public RegisteredUserDTO registerOneUser(SaveUserDTO saveUserDTO) {
        User user = userService.registerOneUser(saveUserDTO);
        RegisteredUserDTO registeredUserDTO = registeredUserMapper.userToRegisteredUserDTO(user);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        registeredUserDTO.setJwt(jwt);
        return registeredUserDTO;
    }

    // registrar admin
    public RegisteredUserDTO registerOneAdmin(SaveUserDTO saveUserDTO){
        User user = userService.registerOneAdmin(saveUserDTO);
        RegisteredUserDTO registeredUserDTO = registeredUserMapper.userToRegisteredUserDTO(user);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        registeredUserDTO.setJwt(jwt);
        return registeredUserDTO;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("name", user.getName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("authorities", user.getAuthorities());
        return extraClaims;
    }


    // login
    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequestDTO.getEmail(),
                authenticationRequestDTO.getPassword()
        );

        authenticationManager.authenticate(authentication);

        User user = userService.findByEmail(authenticationRequestDTO.getEmail());
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setJwt(jwt);
        return response;
    }


    // validar token
    public boolean validateToken(String jwt) {
        try {
            jwtService.extractEmail(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // encontrar usuario autenticado
    public UserProfileDTO findLoggedInUser() {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String email = (String) auth.getPrincipal();
        User userLogged = userService.findByEmail(email);

        return userProfileMapper.userToUserProfileDTO(userLogged);

    }


}