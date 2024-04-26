package com.esquinaPet.veterinariabackend.domain.mappers;


import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import com.esquinaPet.veterinariabackend.dto.ServiceTypeDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "serviceName", target = "serviceName")
    })
    ServiceTypeDTO serviceTypeToServiceTypeDTO(ServiceType serviceType);

    @InheritInverseConfiguration
    ServiceType serviceTypeDTOToServiceType(ServiceTypeDTO serviceTypeDTO);

    List<ServiceTypeDTO> toServiceTypeDTOList(List<ServiceType> serviceTypeList);
    List<ServiceType> toServiceTypeList(List<ServiceTypeDTO> serviceTypeDTOList);

}
