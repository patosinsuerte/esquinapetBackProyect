package com.esquinaPet.veterinariabackend.domain.services;



import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.dto.SaveUserDTO;

public interface UserService {

    User registerOneUser(SaveUserDTO saveUserDTO);

    User registerOneAdmin(SaveUserDTO saveUserDTO);

    User findByEmail(String email);

    User findUserById(Long id);


}
