package com.esquinaPet.veterinariabackend.domain.services.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;


    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date((EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime());

        // SE RETORNA UN STRING DE JWT
        return Jwts.builder()
                // header
                .header()
                .type("JWT")
                .and()
                //payload
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(extraClaims)
                // signature
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();

    }

    private SecretKey generateKey() {
        byte[] key = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(key);
    }

    public String extractEmail(String jwt) {
        return extraClaims(jwt).getSubject();
    }

    private Claims extraClaims(String jwt) {
        return Jwts.parser().
                verifyWith(generateKey()).build()
                .parseSignedClaims(jwt).getPayload();

    }

//    private Claims extraClaims(String jwt) {
//        try {
//            // Utiliza el método parseClaimsJws para parsear JWTs firmados (JWS)
//            return Jwts.parser().
//                verifyWith(generateKey()).build()
//                .parseSignedClaims(jwt).getPayload();
//        } catch (JwtException e) {
//            // Maneja la excepción JwtException (que incluye MalformedJwtException)
//            System.err.println("Error al analizar el token JWT: " + e.getMessage());
//            throw e; // Re-lanza la excepción para que sea manejada en un nivel superior si es necesario
//        }
//    }
}
