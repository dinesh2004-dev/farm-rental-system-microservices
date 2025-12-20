package com.example.booking_service.configuration;

import com.example.booking_service.filters.GatewayAuthHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private GatewayAuthHeaderFilter gatewayAuthHeaderFilter;

    public SecurityConfig(GatewayAuthHeaderFilter gatewayAuthHeaderFilter) {
        this.gatewayAuthHeaderFilter = gatewayAuthHeaderFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())

                .addFilterBefore(gatewayAuthHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();


    }
}
