package com.esquinaPet.veterinariabackend.domain.mappers;


import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.dto.UserProfileDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "rut", target = "rut"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "appointments", target = "appointments"),
    })
    UserProfileDTO userToUserProfileDTO(User user);


    @InheritInverseConfiguration
    User userProfileDTOToUser(UserProfileDTO userProfileDTO);

    List<UserProfileDTO> toUserProfileDTOList(List<User> userList);

    List<User> toUserList(List<UserProfileDTO> userProfileDTOList);

}