package com.esquinaPet.veterinariabackend.domain.mappers;


import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import com.esquinaPet.veterinariabackend.dto.AppointmentRequestDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentRequestMapper {

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "rut", target = "rut"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "phone", target = "phone"),
            @Mapping(source = "date", target = "date"),
            @Mapping(source = "time", target = "time"),
            @Mapping(source = "isActive", target = "isActive"),
            @Mapping(source = "isAvailable", target = "isAvailable"),
            @Mapping(source = "petName", target = "petName"),
            @Mapping(source = "serviceTypeId", target = "serviceType.id"),
            @Mapping(source = "petId", target = "pet.id")
    })
    Appointment appointmentRequestDTOToAppointment(AppointmentRequestDTO appointmentRequestDTO);

    @InheritInverseConfiguration
    AppointmentRequestDTO appointmentRequestDTOToAppointment(Appointment appointment);

    List<Appointment> toAppointment (List<AppointmentRequestDTO> appointmentRequestDTOList);
    List<AppointmentRequestDTO> toAppointmentRequestDTO(List<Appointment> appointmentList);



}
