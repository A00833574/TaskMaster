package com.todochat.todochat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("removal")
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll() // Permite todas las solicitudes sin autenticación
                )
                .csrf(csrf -> csrf.disable()); // Deshabilita CSRF usando Lambda DSL

        http
            .headers(headers -> headers
                .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; object-src 'none'; style-src 'self' 'unsafe-inline';")
                .and()
                .frameOptions().deny()
                .httpStrictTransportSecurity()
                    .maxAgeInSeconds(31536000) // 1 year
                    .includeSubDomains(true)
                .and()
                .cacheControl().disable() // or configure it according to your needs
            );

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
