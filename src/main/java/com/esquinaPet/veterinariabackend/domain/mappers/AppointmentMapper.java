package com.esquinaPet.veterinariabackend.domain.mappers;


import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import com.esquinaPet.veterinariabackend.dto.AppointmentRequestDTO;
import com.esquinaPet.veterinariabackend.dto.AppointmentResponseDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "rut", target = "rut"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "date", target = "date"),
            @Mapping(source = "time", target = "time"),
            @Mapping(source = "pet", target = "pet"),
            @Mapping(source = "isAvailable", target = "isAvailable"),
            @Mapping(source = "isActive", target = "isActive"),
            @Mapping(source = "petName", target = "petName"),
            @Mapping(source = "serviceType", target = "serviceType")

    })
    AppointmentResponseDTO AppointmentToAppointmentDTO(Appointment appointment);

    @InheritInverseConfiguration
    Appointment AppointmentResponseDTOToappointment(AppointmentResponseDTO appointmentResponseDTO);

    List<AppointmentResponseDTO> toAppointmentDTOList(List<Appointment> appointmentList);

    List<Appointment> toAppointmentList(List<AppointmentResponseDTO> appointmentResponseDTOList);

    
}
