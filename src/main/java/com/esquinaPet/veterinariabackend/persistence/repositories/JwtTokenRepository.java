package com.esquinaPet.veterinariabackend.persistence.repositories;

import com.esquinaPet.veterinariabackend.domain.models.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String jwt);
}
