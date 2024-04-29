package com.esquinaPet.veterinariabackend.infra.config.security.filter;

import com.esquinaPet.veterinariabackend.domain.models.JwtToken;
import com.esquinaPet.veterinariabackend.domain.services.auth.JwtService;
import com.esquinaPet.veterinariabackend.domain.services.impl.UserServiceImpl;
import com.esquinaPet.veterinariabackend.persistence.repositories.JwtTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository jwtRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtener authorization header
        // obtener token
        String jwt = jwtService.extractJwtFromRequest(request);

        if (jwt == null || !StringUtils.hasText(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        //paso nuevo 2.1 obtener token NO expirado y validod esde base de datos
        Optional<JwtToken> token = jwtRepository.findByToken(jwt);

        boolean isValid = validateToken(token);

        if (!isValid){
            filterChain.doFilter(request,response);
            return;
        }

        // obtener el subject y validar el token
        String email = jwtService.extractEmail(jwt);

        // setear el objeto authorization en el securitycontext
        UserDetails user = userService.findByEmail(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                email, null, user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // ejecutar el filtro
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(Optional<JwtToken> optionalToken) {
        if (!optionalToken.isPresent()){
            System.out.println("EL token no fue generado en nuestro sistema");
            return false;
        }

        JwtToken jwtToken = optionalToken.get();

        Date now = new Date(System.currentTimeMillis());

        boolean isValid = jwtToken.getIsValid() && jwtToken.getExpiration().after(now);

        if (!isValid){
            System.out.println("Token invalido");
            updateTokenStatus(jwtToken);
        }
        return isValid;
    }

    private void updateTokenStatus(JwtToken jwtToken) {


        jwtToken.setIsValid(false);
        jwtRepository.save(jwtToken);
    }
}
