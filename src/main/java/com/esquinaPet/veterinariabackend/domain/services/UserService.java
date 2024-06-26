package com.esquinaPet.veterinariabackend.domain.services;



import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.dto.EditUserRequestDTO;
import com.esquinaPet.veterinariabackend.dto.SaveUserDTO;
import com.esquinaPet.veterinariabackend.dto.UserResponseDTO;

public interface UserService {

    User registerOneUser(SaveUserDTO saveUserDTO);

    User registerOneAdmin(SaveUserDTO saveUserDTO);

    User findByEmail(String email);

    User findUserById(Long id);


    boolean emailHasExist(String email);

    boolean phoneHasExist(String phone);

    boolean rutHasExist(String rut);

    boolean passwordHasMatch(String email, String password);

}
