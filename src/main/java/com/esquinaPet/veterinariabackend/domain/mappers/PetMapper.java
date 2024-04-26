package com.esquinaPet.veterinariabackend.domain.mappers;

import com.esquinaPet.veterinariabackend.domain.models.Pet;
import com.esquinaPet.veterinariabackend.dto.PetDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {



    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "petType", target = "petType"),
    })
    PetDTO petToPetDTO(Pet pet);


    @InheritInverseConfiguration
    Pet petDTOToPet(PetDTO petDTO);

    List<PetDTO> toPetDTOList(List<Pet> petList);
    List<Pet> toPetList(List<PetDTO> petDTOList);
}
