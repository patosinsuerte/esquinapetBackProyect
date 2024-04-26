package com.esquinaPet.veterinariabackend.infra.config.security.filter;

import com.esquinaPet.veterinariabackend.domain.services.auth.JwtService;
import com.esquinaPet.veterinariabackend.domain.services.impl.UserServiceImpl;
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


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // primer paso
        String authorizationHeader = request.getHeader("Authorization");
        // validar si viene el token, sino viene ejecutamos el filtro
        // y decimos que siga con su camino pasando el rquest y el response como argumento
        // retornando elc ontrol al hilo de ejecucion
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // obtener el jwt desde el authorization
        String jwt = authorizationHeader.split(" ")[1];

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
}
