package com.esquinaPet.veterinariabackend.domain.services;

import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import com.esquinaPet.veterinariabackend.dto.ServiceTypeDTO;

import java.util.List;

public interface ServiceTypeService{

    // get all typeservice
    List<ServiceTypeDTO> getAllServiceTypes();

    // get typeservice by id
    ServiceType getServiceTypeById(Long id);

    // CREATE SERVICE TYPE
    ServiceTypeDTO createOneServiceType(ServiceType serviceType);
}
