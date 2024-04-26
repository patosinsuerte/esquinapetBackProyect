package com.esquinaPet.veterinariabackend.domain.services;

import com.esquinaPet.veterinariabackend.domain.models.Pet;
import com.esquinaPet.veterinariabackend.dto.PetDTO;

import java.util.List;

public interface PetService {

    // GET ASERVICE BY ID
    Pet findPetById(Long id);
    // GET ALL PET NAME
    List<PetDTO> findAllPet();


}
