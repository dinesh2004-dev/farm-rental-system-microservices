package com.example.payment_service.config;

import com.example.payment_service.filter.GatewayAuthHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final GatewayAuthHeaderFilter gatewayAuthHeaderFilter;

    public SecurityConfig(GatewayAuthHeaderFilter gatewayAuthHeaderFilter) {
        this.gatewayAuthHeaderFilter = gatewayAuthHeaderFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll().anyRequest().permitAll())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .addFilterBefore(gatewayAuthHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


}
