package com.esquinaPet.veterinariabackend.infra.config.security;


import com.esquinaPet.veterinariabackend.domain.utils.enums.Role;
import com.esquinaPet.veterinariabackend.infra.config.security.filter.JwtAuthenticationFilter;
import com.esquinaPet.veterinariabackend.infra.errors.CustomAccessDeniedHandler;
import com.esquinaPet.veterinariabackend.infra.errors.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {


    private final AuthenticationProvider daoAuthentication;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public HttpSecurityConfig(
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        this.daoAuthentication = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthentication)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    buildRequestMatchers(auth);
                })
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(customAuthenticationEntryPoint);
                })
                .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler))
                .build();
    }


    private static void buildRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        // autorizacion de endpoints de appointments

        // AUTORIZACION ENDPOINTS APPOINTMENTS
        auth.requestMatchers(HttpMethod.GET, "/appointments")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

//        auth.requestMatchers(HttpMethod.GET, "/appointments/{id}")
//                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        auth.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "appointments/[0-9]*"))
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());


        auth.requestMatchers(HttpMethod.POST, "/appointments")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.USER.name());

        auth.requestMatchers(HttpMethod.PUT, "/appointments/{id}/canceled")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.USER.name());

//        // AUTORIZACION ENDPOINTS SERVICESTYPE
        auth.requestMatchers(HttpMethod.GET, "/service-types")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());

        // AUTORIZACION ENDPOINTS AUTH
        auth.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.USER.name());
        auth.requestMatchers(HttpMethod.GET, "/auth/appointments")
                .hasRole(Role.USER.name());


        // END POINTS PUBLICOS
        auth.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
        auth.requestMatchers(HttpMethod.POST, "/users/register/user").permitAll();
        auth.requestMatchers(HttpMethod.POST, "/users/register/admin").permitAll();
        auth.anyRequest().authenticated();
    }
}
