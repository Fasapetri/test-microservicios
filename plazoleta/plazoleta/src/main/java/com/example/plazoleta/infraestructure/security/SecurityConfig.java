package com.example.plazoleta.infraestructure.security;

import com.example.plazoleta.infraestructure.adapter.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtFilter jwtFilter, CustomAccessDeniedHandler customAccessDeniedHandler){
        this.jwtFilter = jwtFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/restaurant/addRestaurant").hasAuthority("ADMIN")
                        .requestMatchers("/api/dishes/add-dish", "/api/dishes/{dishId}/update-dish"
                        , "/api/dishes/{dishId}/update-dish-status", "/api/restaurant/asignar-empleado-restaurant",
                                "/api/pedidos/efficiency", "/api/pedidos/ranking-employee")
                        .hasAuthority("PROPIETARIO")
                        .requestMatchers("/api/pedidos/save", "/api/pedidos/update-status-cancelado",
                                "/api/pedidos/trazabilidad").hasAuthority("CLIENTE")
                        .requestMatchers("/api/pedidos/list-status", "/api/pedidos/update-status-preparacion",
                                "/api/pedidos/update-status-listo", "/api/pedidos/update-status-entregado").hasAuthority("EMPLEADO")
                        .requestMatchers("/api/restaurant/**").authenticated()
                        .requestMatchers("/api/dishes/**").authenticated()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                ).exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
