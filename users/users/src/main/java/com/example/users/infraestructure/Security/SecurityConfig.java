package com.example.users.infraestructure.security;

import com.example.users.infraestructure.adapter.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    public SecurityConfig(JwtFilter jwtFilter, CustomAccessDeniedHandler customAccessDeniedHandler){
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/api/auth/login", "/swagger-ui/**",
                                "/swagger-ui.html", "/api/users/create-user-cliente").permitAll()
                        .requestMatchers("/api/users/create-user-propietario").hasAuthority("ADMIN")
                        .requestMatchers("/api/users/create-user-empleado").hasAuthority("PROPIETARIO")
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/auth/**").authenticated()
                        .anyRequest().authenticated()
                ).exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
