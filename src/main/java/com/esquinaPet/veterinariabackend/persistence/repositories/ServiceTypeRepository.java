package com.esquinaPet.veterinariabackend.persistence.repositories;


import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
}
