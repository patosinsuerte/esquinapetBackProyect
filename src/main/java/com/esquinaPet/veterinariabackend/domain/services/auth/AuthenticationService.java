package com.esquinaPet.veterinariabackend.domain.services.auth;

import com.esquinaPet.veterinariabackend.domain.mappers.RegisteredUserMapper;
import com.esquinaPet.veterinariabackend.domain.mappers.ResponseUserMapper;
import com.esquinaPet.veterinariabackend.domain.mappers.UserProfileMapper;
import com.esquinaPet.veterinariabackend.domain.models.JwtToken;
import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.domain.services.impl.UserServiceImpl;
import com.esquinaPet.veterinariabackend.dto.*;
import com.esquinaPet.veterinariabackend.infra.exceptions.EmailHasNotExistException;
import com.esquinaPet.veterinariabackend.infra.exceptions.WrongPasswordException;
import com.esquinaPet.veterinariabackend.persistence.repositories.JwtTokenRepository;
import com.esquinaPet.veterinariabackend.persistence.repositories.UserRepository;
import com.esquinaPet.veterinariabackend.shared.utils.AddCountryCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserServiceImpl  userService;
    private final RegisteredUserMapper registeredUserMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserProfileMapper userProfileMapper;
    private final JwtTokenRepository jwtTokenRepository;
    private final ResponseUserMapper responseUserMapper;
    private final UserRepository userRepository;
    private final AddCountryCode addCountryCode;

    @Autowired
    public AuthenticationService(
            UserServiceImpl userService,
            RegisteredUserMapper registeredUserMapper,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserProfileMapper userProfileMapper,
            JwtTokenRepository jwtTokenRepository,
            ResponseUserMapper responseUserMapper,
            UserRepository userRepository,
            AddCountryCode addCountryCode
    ) {
        this.userService = userService;
        this.registeredUserMapper = registeredUserMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userProfileMapper = userProfileMapper;
        this.jwtTokenRepository = jwtTokenRepository;
        this.responseUserMapper = responseUserMapper;
        this.userRepository = userRepository;
        this.addCountryCode = addCountryCode;
    }


    // register user
    public RegisteredUserDTO registerOneUser(SaveUserDTO saveUserDTO) {

        User user = userService.registerOneUser(saveUserDTO);
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        this.saveUserToken(user, jwt);
        RegisteredUserDTO registeredUserDTO = registeredUserMapper.userToRegisteredUserDTO(user);
        registeredUserDTO.setJwt(jwt);
        return registeredUserDTO;
    }

    // registrar admin
    public RegisteredUserDTO registerOneAdmin(SaveUserDTO saveUserDTO) {
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
        if (!this.userService.emailHasExist(authenticationRequestDTO.getEmail())) {
            throw new EmailHasNotExistException("El correo que ingrsaste no existe, por favor ingresa un correo registrado");
        }
        if (!this.userService.passwordHasMatch(authenticationRequestDTO.getEmail(), authenticationRequestDTO.getPassword())) {
            throw new WrongPasswordException("La constrasena que ingreso no coincide, por favor intente de nuevo");
        }
        authenticationManager.authenticate(authentication);
        User user = userService.findByEmail(authenticationRequestDTO.getEmail());
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        this.saveUserToken(user, jwt);
        AuthenticationResponseDTO response = new AuthenticationResponseDTO();
        response.setJwt(jwt);
        response.setUser(responseUserMapper.userToUserResponseDTO(user));
        return response;
    }

    private void saveUserToken(User user, String jwt) {

        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setIsValid(true);

        jwtTokenRepository.save(token);
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


    public void logout(HttpServletRequest httpServletRequest) {

        String jwt = jwtService.extractJwtFromRequest(httpServletRequest);

        if (jwt == null || !StringUtils.hasText(jwt)) return;

        Optional<JwtToken> token = jwtTokenRepository.findByToken(jwt);

        if (token.isPresent() && token.get().getIsValid()) {
            token.get().setIsValid(false);
            System.out.println(token);
            jwtTokenRepository.save(token.get());
        }
    }



    public UserResponseDTO editLoggedUserInfo(EditUserRequestDTO editUserRequestDTO){

        UserProfileDTO userProfileDTO = this.findLoggedInUser();

        User userDB = userService.findUserById(userProfileDTO.getId());


        if(editUserRequestDTO.getName() != null){
            userDB.setName(editUserRequestDTO.getName());
        }

        if (editUserRequestDTO.getPhone() != null) {

            String phoneWithCode = this.addCountryCode.addCountryCode(editUserRequestDTO.getPhone());

            userDB.setPhone(phoneWithCode);
        }

        if (editUserRequestDTO.getLastName() != null) {
            userDB.setLastName(editUserRequestDTO.getLastName());
        }

        this.userRepository.save(userDB);


        return this.responseUserMapper.userToUserResponseDTO(userDB);

    }






}
