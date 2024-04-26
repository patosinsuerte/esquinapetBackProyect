package com.esquinaPet.veterinariabackend.domain.services.impl;

import com.esquinaPet.veterinariabackend.domain.mappers.PetMapper;
import com.esquinaPet.veterinariabackend.domain.models.Pet;
import com.esquinaPet.veterinariabackend.persistence.repositories.PetRepository;
import com.esquinaPet.veterinariabackend.domain.services.PetService;
import com.esquinaPet.veterinariabackend.dto.PetDTO;
import com.esquinaPet.veterinariabackend.infra.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {


    // PROPERTIES
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Autowired
    public PetServiceImpl(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }


    // fin pet typ id
    @Override
    public Pet findPetById(Long id) {

        return petRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pet type not found with id: " + id));
    }

    // get all pet types
    @Override
    public List<PetDTO> findAllPet() {
        List<Pet> petListDB = petRepository.findAll();
        if (petListDB.isEmpty()) {
            return Collections.emptyList();
        }
        return petMapper.toPetDTOList(petListDB);
    }
}
