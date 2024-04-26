package com.esquinaPet.veterinariabackend.domain.mappers;

import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.dto.SaveUserDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaveUserMapper {


    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "rut", target = "rut"),
            @Mapping(source = "password", target = "password")
    })
    User saveUserDTOToUser(SaveUserDTO saveUserDTO);

    @InheritInverseConfiguration
    SaveUserDTO userToSaveDTO(User user);

    List<User> saveUserDTOToUserList(List<SaveUserDTO> saveUserDTOList);
    List<SaveUserDTO> userListToSaveUserDTO(List<User> userList);

}
