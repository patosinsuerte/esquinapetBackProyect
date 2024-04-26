package com.esquinaPet.veterinariabackend.domain.models;


import com.esquinaPet.veterinariabackend.domain.utils.enums.AvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    private String name;
    @Column(name = "last_name")
    @NotNull
    private String lastName;
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}-[\\dk]$")
    @Column(unique = true)
    @Size(min = 12, max = 12)
    private String rut;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$")
    private String email;
    @NotNull
    @Pattern(regexp = "^\\+569\\d{8}$")
    @Size(min = 12, max = 12)
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    @NotNull
    private LocalTime time;
    @Column(name = "is_available")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus isAvailable;
    @Column(name = "created_at")
    @NotNull
    private Timestamp createdAt;
    @OneToOne
    @JoinColumn(name = "service_types_id")
    @NotNull
    private ServiceType serviceType;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @OneToOne
    @NotNull
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
