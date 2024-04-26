package com.esquinaPet.veterinariabackend.domain.models;


import com.esquinaPet.veterinariabackend.domain.utils.enums.ServiceName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "service_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "service_name")
    @Enumerated(EnumType.STRING)
    private ServiceName serviceName;

}
