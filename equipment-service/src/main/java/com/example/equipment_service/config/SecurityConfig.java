package com.example.equipment_service.config;

import com.example.equipment_service.filters.GatewayAuthHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

        http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                    auth.anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable());

        http.addFilterBefore(gatewayAuthHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
