package com.esquinaPet.veterinariabackend.persistence.repositories;

import com.esquinaPet.veterinariabackend.domain.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
