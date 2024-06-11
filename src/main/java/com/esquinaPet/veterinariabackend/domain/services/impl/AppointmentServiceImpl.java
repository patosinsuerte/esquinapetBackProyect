package com.esquinaPet.veterinariabackend.domain.services.impl;

import com.esquinaPet.veterinariabackend.domain.services.auth.AuthenticationService;
import com.esquinaPet.veterinariabackend.domain.mappers.AppointmentMapper;
import com.esquinaPet.veterinariabackend.domain.mappers.AppointmentRequestMapper;
import com.esquinaPet.veterinariabackend.domain.models.Appointment;
import com.esquinaPet.veterinariabackend.domain.models.Pet;
import com.esquinaPet.veterinariabackend.domain.models.ServiceType;
import com.esquinaPet.veterinariabackend.domain.models.User;
import com.esquinaPet.veterinariabackend.infra.exceptions.*;
import com.esquinaPet.veterinariabackend.persistence.repositories.AppointmentRepository;
import com.esquinaPet.veterinariabackend.domain.services.AppointmentService;
import com.esquinaPet.veterinariabackend.domain.utils.enums.AvailabilityStatus;
import com.esquinaPet.veterinariabackend.dto.AppointmentRequestDTO;
import com.esquinaPet.veterinariabackend.dto.AppointmentResponseDTO;
import com.esquinaPet.veterinariabackend.dto.UserProfileDTO;
import com.esquinaPet.veterinariabackend.shared.services.TimesService;
import com.esquinaPet.veterinariabackend.shared.utils.AddCountryCode;
import com.esquinaPet.veterinariabackend.shared.utils.AllowedDates;
import com.esquinaPet.veterinariabackend.shared.utils.AllowedTimes;
import com.esquinaPet.veterinariabackend.shared.utils.ValidateBeforeDates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final TimesService timesService;
    private final ValidateBeforeDates validateBeforeDates;
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
            AddCountryCode addCountryCode,
            TimesService timesService,
            ValidateBeforeDates validateBeforeDates
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
        this.timesService = timesService;
        this.validateBeforeDates = validateBeforeDates;
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


    //* CREATE NEW APPOINTMENT USUARIO LOGGEADO
    @Override
    public AppointmentResponseDTO createNewAppointment(AppointmentRequestDTO appointmentRequestDTO) {

        //* VALIDAR DATE Y TIME
        if (!allowedTimes.validateTime(appointmentRequestDTO.getTime())) {
            throw new InvalidAppointmentTimeException("La hora seleccionada no es válida: " + appointmentRequestDTO.getTime());
        }

        if (!this.validateBeforeDates.verifyIfDateIsBeforeToActual(appointmentRequestDTO.getDate())){
            throw new InvalidAppointmentDateException("La fecha seleccionada no es valida: " + appointmentRequestDTO.getDate());
        }

        if (!allowedDates.validateDateWithAllowedDates(appointmentRequestDTO.getDate())) {
            throw new InvalidAppointmentDateException("La fecha seleccionada no es valida: " + appointmentRequestDTO.getDate());
        }

        if (this.timeAvailabilityValidation(appointmentRequestDTO.getTime(), appointmentRequestDTO.getDate())){
            throw new TimeIsNotAvailable("La hora no esta disponible", HttpStatus.CONFLICT.toString(), "time");
        }

        //* OBTENER INSTANCIAS FALTANTES
        Appointment newAppointment = appointmentRequestMapper.appointmentRequestDTOToAppointment(appointmentRequestDTO);
        ServiceType serviceTypeDB = serviceTypeService.getServiceTypeById(newAppointment.getServiceType().getId());
        Pet petTypeDB = petService.findPetById(newAppointment.getPet().getId());
        // obtener usuario logeado
        UserProfileDTO userProfileDTO = authenticationService.findLoggedInUser();
        User userDB = userService.findUserById(userProfileDTO.getId());

        // validar correo y
        this.userInfoMatch(appointmentRequestDTO, userProfileDTO);


        // setear los valores falantes
        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);
        newAppointment.setIsAvailable(AvailabilityStatus.RESERVED);
        newAppointment.setServiceType(serviceTypeDB);
        newAppointment.setPet(petTypeDB);
        newAppointment.setCreatedAt(timestamp);
        newAppointment.setUser(userDB);
        newAppointment.setIsActive(true);
        newAppointment.setPhone(addCountryCode.addCountryCode(newAppointment.getPhone()));
        //GUARDAR APPOINTMENT
        appointmentRepository.save(newAppointment);
        // convertir a DTO
        return appointmentMapper.AppointmentToAppointmentDTO(newAppointment);
    }


    //* CANCEL APPOINTMENT
    @Override
    public Boolean cancelAppointmentById(Long id) {
        Appointment appointmentDB = appointmentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Appintment not found with id: " + id));
        appointmentDB.setIsAvailable(AvailabilityStatus.AVAILABLE);
        appointmentRepository.save(appointmentDB);
        return true;
    }


    /********************** USUARIO AUTENTICADO APPOINTMENT LOGIC*////////////////////*
    //* OBTENER CITAS MEDICAS DEL USUARIO AUTENTICADO
    @Override
    public List<AppointmentResponseDTO> findUserLoggedOwnAppointments() {
        //obtener usuario logeado
        UserProfileDTO userProfileDTO = authenticationService.findLoggedInUser();
        User userLogged = userService.findUserById(userProfileDTO.getId());
        List<Appointment> appointmentResponseDTOS = userLogged.getAppointments();



        // Filtrar las citas para obtener solo las que tienen el estado "RESERVED"
        List<Appointment> reservedAppointments = appointmentResponseDTOS.stream()
                .filter(appointment -> appointment.getIsAvailable() == AvailabilityStatus.RESERVED)
                .collect(Collectors.toList());

        // Mapear las citas filtradas a DTOs y devolverlas
        return appointmentMapper.toAppointmentDTOList(reservedAppointments);
    }


    /*CANCELAR CITAS DEL USUARIO AUTENTICADO*/
    @Override
    public Boolean cancelAppointmentByAuthenticatedUser(Long id) {
        List<AppointmentResponseDTO> appointmentResponseDTOList = this.findUserLoggedOwnAppointments();

        // Buscar la cita con el ID especificado en la lista de citas del usuario autenticado
        Optional<AppointmentResponseDTO> appointmentOptional = appointmentResponseDTOList.stream()
                .filter(appointment -> appointment.getId().equals(id))
                .findFirst();

        // Verificar si se encontró la cita
        if (appointmentOptional.isPresent()) {
            // Obtener la cita desde la base de datos
            Appointment appointmentDB = appointmentRepository.findById(appointmentOptional.get().getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Cita no encontrada con el ID: " + id));

            // Actualizar el estado de disponibilidad de la cita
            appointmentDB.setIsAvailable(AvailabilityStatus.CANCELLED);
            appointmentRepository.save(appointmentDB);

            // Devolver un mensaje de éxito
            return  true;
        } else {
            // Devolver un mensaje de error si la cita no fue encontrada
            return false;
        }
    }

    @Override
    public List<AppointmentResponseDTO> getAllUserAppointmentsActives() {
        List<AppointmentResponseDTO> appointmentResponseDTOList = this.findUserLoggedOwnAppointments();

        List<AppointmentResponseDTO> activeAppointments = appointmentResponseDTOList.stream().filter(AppointmentResponseDTO::getIsActive).toList();

        return activeAppointments;
    }


    //validaciones de datos del usuario para crear una hora
    private boolean userInfoMatch(AppointmentRequestDTO appointmentRequestDTO, UserProfileDTO userProfileDTO) {
        if (!appointmentRequestDTO.getName().matches(userProfileDTO.getName())) {
            throw new UserInfoNotMatchException("Los campos no coinciden", "name", appointmentRequestDTO.getName(), userProfileDTO.getName());
        }

        if (!appointmentRequestDTO.getRut().matches(userProfileDTO.getRut())) {
            throw new UserInfoNotMatchException("Los campos no coinciden", "rut", appointmentRequestDTO.getRut(), userProfileDTO.getRut());
        }
        if (!appointmentRequestDTO.getEmail().matches(userProfileDTO.getEmail())) {
            throw new UserInfoNotMatchException("Los campos no coinciden", "rut", appointmentRequestDTO.getRut(), userProfileDTO.getRut());
        }

        if (!appointmentRequestDTO.getLastName().matches(userProfileDTO.getLastName())) {
            throw new UserInfoNotMatchException("Los campos no coinciden", "lastName", appointmentRequestDTO.getLastName(), userProfileDTO.getLastName());
        }

        String extractPhone = userProfileDTO.getPhone().substring(3);

        if (!appointmentRequestDTO.getPhone().matches(extractPhone)) {
            throw new UserInfoNotMatchException("Los campos no coinciden", "phone", appointmentRequestDTO.getPhone(), userProfileDTO.getPhone().substring(3));
        }
        return true;

    }



    private boolean timeAvailabilityValidation(LocalTime time, LocalDate date) {
        List<LocalTime> times = timesService.findReservedTimesByDate(date);
        if(times.contains(time)){
            return true;
        }
        return false;
    }


}





