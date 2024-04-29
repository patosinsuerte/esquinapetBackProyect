package com.esquinaPet.veterinariabackend.domain.services.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public String extractJwtFromRequest(HttpServletRequest request) {
        // primer paso
        String authorizationHeader = request.getHeader("Authorization");
        // validar si viene el token, sino viene ejecutamos el filtro
        // y decimos que siga con su camino pasando el rquest y el response como argumento
        // retornando elc ontrol al hilo de ejecucion
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        // obtener el jwt desde el authorization
        return authorizationHeader.split(" ")[1];
    }

    public Date extractExpiration(String jwt) {
        return extraClaims(jwt).getExpiration();
    }
}
