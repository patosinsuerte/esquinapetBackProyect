package com.esquinaPet.veterinariabackend.domain.services.impl;


import com.esquinaPet.veterinariabackend.domain.mappers.SaveUserMapper;
import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.persistence.repositories.UserRepository;
import com.esquinaPet.veterinariabackend.domain.services.UserService;
import com.esquinaPet.veterinariabackend.domain.utils.enums.Role;
import com.esquinaPet.veterinariabackend.dto.SaveUserDTO;
import com.esquinaPet.veterinariabackend.infra.exceptions.InvalidPasswordException;
import com.esquinaPet.veterinariabackend.infra.exceptions.ObjectNotFoundException;
import com.esquinaPet.veterinariabackend.shared.utils.AddCountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        // Validar contrasena
        this.validatePassword(saveUserDTO);
        User newUser = saveUserMapper.saveUserDTOToUser(saveUserDTO);
        newUser.setRole(Role.USER);
        newUser.setPhone(addCountryCode.addCountryCode(saveUserDTO.getPhone()));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        System.out.println("SE IMPRIME EL NUMERO: " + newUser.getPhone());
        return userRepository.save(newUser);
    }

    // CREATE / REGISTER ONE ADMIN
    @Override
    public User registerOneAdmin(SaveUserDTO saveUserDTO) {
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


    // METODOS PRIVADOS DE UTILIDAD
    // metodo para validar contrasena
    private void validatePassword(SaveUserDTO saveUserDTO){
        if(!StringUtils.hasText(saveUserDTO.getPassword()) ||
                !StringUtils.hasText(saveUserDTO.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
        if(!saveUserDTO.getPassword()
                .equals(saveUserDTO.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }



}
