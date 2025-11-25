package com.senai.conta_bancaria.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(
                        AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
                        //CLIENTE
                        .requestMatchers(HttpMethod.POST, "/cliente").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/cliente").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/cliente").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/cliente").hasRole("GERENTE")

                        //CONTA
                        .requestMatchers(HttpMethod.POST, "/conta").hasAnyRole("GERENTE","CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/conta").hasAnyRole("GERENTE","CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/conta").hasAnyRole("GERENTE","CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/conta").hasAnyRole("GERENTE")

                        //GERENTE
                        .requestMatchers(HttpMethod.POST, "/gerente").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/gerente").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/gerente").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/gerente").hasRole("ADMIN")

                        //PAGAMENTO
                        .requestMatchers(HttpMethod.POST, "/contas/{numeroConta}/pagamentos").hasAnyRole("CLIENTE", "GERENTE")

                        //TAXAS
                        .requestMatchers(HttpMethod.POST, "/taxas").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/taxas/**").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/taxas/**").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/taxas/{id}").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/taxas").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(usuarioDetailsService);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

