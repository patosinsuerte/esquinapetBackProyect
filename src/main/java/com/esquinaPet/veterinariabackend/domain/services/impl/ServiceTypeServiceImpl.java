package com.esquinaPet.veterinariabackend.domain.services.impl;

import com.esquinaPet.veterinariabackend.domain.mappers.ServiceTypeMapper;
import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import com.esquinaPet.veterinariabackend.persistence.repositories.ServiceTypeRepository;
import com.esquinaPet.veterinariabackend.domain.services.ServiceTypeService;
import com.esquinaPet.veterinariabackend.dto.ServiceTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    @Autowired
    public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository, ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceTypeMapper = serviceTypeMapper;
    }



    @Override
    public List<ServiceTypeDTO> getAllServiceTypes() {
        List<ServiceType> serviceTypeListDB = serviceTypeRepository.findAll();
        return serviceTypeMapper.toServiceTypeDTOList(serviceTypeListDB);
    }

    @Override
    public ServiceType getServiceTypeById(Long id) {
        return serviceTypeRepository.findById(id).orElse(null);
    }

    @Override
    public ServiceTypeDTO createOneServiceType(ServiceType serviceType) {
        return serviceTypeMapper.serviceTypeToServiceTypeDTO(serviceType);
    }


}
