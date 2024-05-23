package com.esquinaPet.veterinariabackend.domain.services.impl;


import com.esquinaPet.veterinariabackend.domain.mappers.SaveUserMapper;
import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.infra.exceptions.UserAlreadyExistsException;
import com.esquinaPet.veterinariabackend.persistence.repositories.UserRepository;
import com.esquinaPet.veterinariabackend.domain.services.UserService;
import com.esquinaPet.veterinariabackend.domain.utils.enums.Role;
import com.esquinaPet.veterinariabackend.dto.SaveUserDTO;
import com.esquinaPet.veterinariabackend.infra.exceptions.InvalidPasswordException;
import com.esquinaPet.veterinariabackend.infra.exceptions.ObjectNotFoundException;
import com.esquinaPet.veterinariabackend.shared.utils.AddCountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final SaveUserMapper saveUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AddCountryCode addCountryCode;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            SaveUserMapper saveUserMapper,
            PasswordEncoder passwordEncoder,
            AddCountryCode addCountryCode
    ) {
        this.userRepository = userRepository;
        this.saveUserMapper = saveUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.addCountryCode = addCountryCode;
    }


    // create / register one user
    /*
        Se devuelve un User y no un UserResponseDTO, porque se transforma
        este User en un RegisteredUserDTO en el AuthenticationService
    * */
    @Override
    public User registerOneUser(SaveUserDTO saveUserDTO) {
        boolean emailHasExist = this.emailHasExist(saveUserDTO.getEmail());
        boolean phoneHasExist = this.phoneHasExist(saveUserDTO.getPhone());
        boolean rutHasExist = this.rutHasExist(saveUserDTO.getRut());

        if (emailHasExist) {
            throw new UserAlreadyExistsException("Email: " + saveUserDTO.getEmail() + " already exist", "email");
        }
        if (phoneHasExist) {
            throw new UserAlreadyExistsException("Phone: " + saveUserDTO.getPhone() + " already exist", "phone");
        }
        if (rutHasExist) {
            throw new UserAlreadyExistsException("Rut: " + saveUserDTO.getRut() + " already exist", "rut");
        }


        this.validatePassword(saveUserDTO);
        User newUser = saveUserMapper.saveUserDTOToUser(saveUserDTO);
        newUser.setRole(Role.USER);
        newUser.setPhone(addCountryCode.addCountryCode(saveUserDTO.getPhone()));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);


        // Validar contrasena

    }

    // CREATE / REGISTER ONE ADMIN
    @Override
    public User registerOneAdmin(SaveUserDTO saveUserDTO) {
        boolean emailHasExist = this.emailHasExist(saveUserDTO.getEmail());
        boolean phoneHasExist = this.phoneHasExist(saveUserDTO.getPhone());
        boolean rutHasExist = this.rutHasExist(saveUserDTO.getRut());
        if (emailHasExist) {
            throw new UserAlreadyExistsException("Email: " + saveUserDTO.getEmail() + " already exist", "email");
        }
        if (phoneHasExist) {
            throw new UserAlreadyExistsException("Phone: " + saveUserDTO.getPhone() + " already exist", "phone");
        }
        if (rutHasExist) {
            throw new UserAlreadyExistsException("Rut: " + saveUserDTO.getRut() + " already exist", "rut");
        }

        // Validar contrasena
        this.validatePassword(saveUserDTO);
        User newAdmin = saveUserMapper.saveUserDTOToUser(saveUserDTO);
        newAdmin.setRole(Role.ADMINISTRATOR);
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        newAdmin.setPhone(addCountryCode.addCountryCode(saveUserDTO.getPhone()));
        return userRepository.save(newAdmin);
    }


    // Find user by email
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with email: " + email));
    }


    // find user by id
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("User not found with id: " + id)
        );
    }

    @Override
    public boolean emailHasExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean phoneHasExist(String phone) {
        return userRepository.findByPhone("+56"+phone).isPresent();
    }

    @Override
    public boolean rutHasExist(String rut) {
        return userRepository.findByRut(rut).isPresent();
    }


    // METODOS PRIVADOS DE UTILIDAD
    // metodo para validar contrasena
    private void validatePassword(SaveUserDTO saveUserDTO) {
        if (!StringUtils.hasText(saveUserDTO.getPassword()) ||
                !StringUtils.hasText(saveUserDTO.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords don't match");
        }
        if (!saveUserDTO.getPassword()
                .equals(saveUserDTO.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords don't match");
        }
    }


}
