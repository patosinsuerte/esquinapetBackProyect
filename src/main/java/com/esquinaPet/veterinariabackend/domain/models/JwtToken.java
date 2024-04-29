package com.esquinaPet.veterinariabackend.domain.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "jwt_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2048)
    private String token;
    private Date expiration;
    @Column(name = "is_valid")
    private Boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
