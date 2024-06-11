package com.esquinaPet.veterinariabackend.domain.mappers;


import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.dto.UserResponseDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResponseUserMapper {

    @Mappings({

            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "rut", target = "rut"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "isActive", target = "isActive"),
            @Mapping(source = "appointments", target = "appointments"),
    })
    UserResponseDTO userToUserResponseDTO(User user);

    @InheritInverseConfiguration
    User userResponseDTOToUser(UserResponseDTO userResponseDTO);

    List<UserResponseDTO> toUserResponseDTOList(List<User> userList);
    List<User> toUserList(List<UserResponseDTO> userResponseDTOList);

}
