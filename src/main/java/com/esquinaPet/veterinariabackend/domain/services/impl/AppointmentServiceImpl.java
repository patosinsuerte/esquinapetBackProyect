package com.esquinaPet.veterinariabackend.domain.services.impl;

import com.esquinaPet.veterinariabackend.domain.services.auth.AuthenticationService;
import com.esquinaPet.veterinariabackend.domain.mappers.AppointmentMapper;
import com.esquinaPet.veterinariabackend.domain.mappers.AppointmentRequestMapper;
import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import com.esquinaPet.veterinariabackend.domain.models.Pet;
import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.persistence.repositories.AppointmentRepository;
import com.esquinaPet.veterinariabackend.domain.services.AppointmentService;
import com.esquinaPet.veterinariabackend.domain.utils.enums.AvailabilityStatus;
import com.esquinaPet.veterinariabackend.dto.AppointmentRequestDTO;
import com.esquinaPet.veterinariabackend.dto.AppointmentResponseDTO;
import com.esquinaPet.veterinariabackend.dto.UserProfileDTO;
import com.esquinaPet.veterinariabackend.infra.exceptions.InvalidAppointmentDateException;
import com.esquinaPet.veterinariabackend.infra.exceptions.InvalidAppointmentTimeException;
import com.esquinaPet.veterinariabackend.infra.exceptions.ObjectNotFoundException;
import com.esquinaPet.veterinariabackend.shared.utils.AddCountryCode;
import com.esquinaPet.veterinariabackend.shared.utils.AllowedDates;
import com.esquinaPet.veterinariabackend.shared.utils.AllowedTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final ServiceTypeServiceImpl serviceTypeService;
    private final AppointmentRequestMapper appointmentRequestMapper;
    private final UserServiceImpl userService;
    private final AuthenticationService authenticationService;
    private final PetServiceImpl petService;
    private final AllowedTimes allowedTimes;
    private final AllowedDates allowedDates;
    private final AddCountryCode addCountryCode;


    //CONSTRUCTOR
    @Autowired
    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper,
            ServiceTypeServiceImpl serviceTypeService,
            AppointmentRequestMapper appointmentRequestMapper,
            UserServiceImpl userService,
            AuthenticationService authenticationService,
            PetServiceImpl petService,
            AllowedTimes allowedTimes,
            AllowedDates allowedDates,
            AddCountryCode addCountryCode
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.serviceTypeService = serviceTypeService;
        this.appointmentRequestMapper = appointmentRequestMapper;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.petService = petService;
        this.allowedTimes = allowedTimes;
        this.allowedDates = allowedDates;
        this.addCountryCode = addCountryCode;
    }


    // METHODS
    // GET ALL APPOINTMENTS
    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointmentListDB = appointmentRepository.findAll();
        return appointmentMapper.toAppointmentDTOList(appointmentListDB);
    }


    //* GET ALL APOINTMENTS BY ID
    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByUserId(Long userId) {
        if (userId == null) {
            throw new ObjectNotFoundException("User ID cannot be null");
        }
        List<Appointment> appointmentListDB = appointmentRepository.findAllById(userId);
        if (appointmentListDB.isEmpty()) {
            return Collections.emptyList();
        }
        return appointmentMapper.toAppointmentDTOList(appointmentListDB);
    }


    //* GET APPOINTMENT BY ID
    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointmentDB = appointmentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found with id: " + id));
        return appointmentMapper.AppointmentToAppointmentDTO(appointmentDB);
    }


    //* GET APPOINTMENT BY DATE
    @Override
    public AppointmentResponseDTO getAppointmentsByDate(LocalDate date) {
        Appointment appointmentDB = appointmentRepository.findAppointmentByDate(date)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found with date: " + date));
        return appointmentMapper.AppointmentToAppointmentDTO(appointmentDB);
    }


    //* GET ALL APPOINTMENTS BY DATE
    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByDate(LocalDate date) {
        List<Appointment> appointmentListDB = appointmentRepository.findAllByDate(date);
        if (appointmentListDB.isEmpty()) {
            return Collections.emptyList();
        }
        return appointmentMapper.toAppointmentDTOList(appointmentListDB);
    }


    //* CREATE NEW APPOINTMENT
    @Override
    public AppointmentResponseDTO createNewAppointment(AppointmentRequestDTO appointmentRequestDTO) {

        //* VALIDAR DATE Y TIME
        if (!allowedTimes.validateTime(appointmentRequestDTO.getTime())) {
            throw new InvalidAppointmentTimeException("La hora seleccionada no es válida: " + appointmentRequestDTO.getTime());
        }
        if (!allowedDates.validateDateWithAllowedDates(appointmentRequestDTO.getDate())) {
            throw new InvalidAppointmentDateException("La fecha seleccionada no es valida: " + appointmentRequestDTO.getDate());
        }

        //* OBTENER INSTANCIAS FALTANTES
        Appointment newAppointment = appointmentRequestMapper.appointmentRequestDTOToAppointment(appointmentRequestDTO);
        ServiceType serviceTypeDB = serviceTypeService.getServiceTypeById(newAppointment.getServiceType().getId());
        Pet petTypeDB = petService.findPetById(newAppointment.getPet().getId());
        // obtener usuario logeado
        UserProfileDTO userProfileDTO = authenticationService.findLoggedInUser();
        User userDB = userService.findUserById(userProfileDTO.getId());
        // setear los valores falantes
        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);
        newAppointment.setIsAvailable(AvailabilityStatus.RESERVED);
        newAppointment.setServiceType(serviceTypeDB);
        newAppointment.setPet(petTypeDB);
        newAppointment.setCreatedAt(timestamp);
        newAppointment.setUser(userDB);
        newAppointment.setPhone(addCountryCode.addCountryCode(newAppointment.getPhone()));
        //GUARDAR APPOINTMENT
        appointmentRepository.save(newAppointment);
        // convertir a DTO
        return appointmentMapper.AppointmentToAppointmentDTO(newAppointment);
    }


    //* CANCEL APPOINTMENT
    @Override
    public AppointmentResponseDTO cancelAppointmentById(Long id) {
        Appointment appointmentDB = appointmentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Appintment not found with id: " + id));
        appointmentDB.setIsAvailable(AvailabilityStatus.AVAILABLE);
        appointmentRepository.save(appointmentDB);
        return appointmentMapper.AppointmentToAppointmentDTO(appointmentDB);
    }


    /********************** USUARIO AUTENTICADO*////////////////////*

    //* OBTENER CITAS MEDICAS DEL USUARIO AUTENTICADO
    @Override
    public List<AppointmentResponseDTO> findUserLoggedOwnAppointments() {
        //obtener usuario logeado
        UserProfileDTO userProfileDTO = authenticationService.findLoggedInUser();
        User userLogged = userService.findUserById(userProfileDTO.getId());
        List<Appointment> appointmentResponseDTOS = userLogged.getAppointments();
        return appointmentMapper.toAppointmentDTOList(appointmentResponseDTOS);
    }


    /*CANCELAR CITAS DEL USUARIO AUTENTICADO*/
    @Override
    public AppointmentResponseDTO cancelAppointmentByAuthenticatedUser(Long id) {
        List<AppointmentResponseDTO> appointmentResponseDTOList = this.findUserLoggedOwnAppointments();
        AppointmentResponseDTO appointmentToCancel = appointmentResponseDTOList.stream()
                .filter(appointment -> appointment.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found with id: " + id));

        AppointmentResponseDTO canceledAppointment = cancelAppointmentById(appointmentToCancel.getId());
        return canceledAppointment;
    }


}
