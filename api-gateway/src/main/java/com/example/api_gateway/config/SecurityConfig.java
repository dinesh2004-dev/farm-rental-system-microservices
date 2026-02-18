package com.example.api_gateway.config;

import com.example.api_gateway.security.BearerServerAuthenticationConverter;
import com.example.api_gateway.security.JWTAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JWTAuthenticationManager authenticationManager;
    private final BearerServerAuthenticationConverter bearerConverter;

    public SecurityConfig(JWTAuthenticationManager authenticationManager,
                          BearerServerAuthenticationConverter bearerConverter) {
        this.authenticationManager = authenticationManager;
        this.bearerConverter = bearerConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){

        AuthenticationWebFilter bearerAuthFilter = new AuthenticationWebFilter((ReactiveAuthenticationManager) authenticationManager);
        bearerAuthFilter.setServerAuthenticationConverter(bearerConverter);

        // stateless: do not persist security context to session / server
        bearerAuthFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        // success handler: continue the web filter chain
        bearerAuthFilter.setAuthenticationSuccessHandler(new WebFilterChainServerAuthenticationSuccessHandler());

        // 🔑 DO NOT authenticate OPTIONS
        bearerAuthFilter.setRequiresAuthenticationMatcher(exchange -> {
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return ServerWebExchangeMatcher.MatchResult.notMatch();
            }
            return ServerWebExchangeMatcher.MatchResult.match();
        });

        // failure handler: return 401
        bearerAuthFilter.setAuthenticationFailureHandler(new ServerAuthenticationFailureHandler() {
            @Override
            public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, org.springframework.security.core.AuthenticationException exception) {
                webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return webFilterExchange.getExchange().getResponse().setComplete();
            }
        });

        http
                .cors(cors  -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .pathMatchers("/api/auth/login","/users/save").permitAll()
                            .anyExchange().authenticated())
                .addFilterAt(bearerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(httpBasicSpec -> httpBasicSpec.disable()) // disable if you don't want basic
                .formLogin(form -> form.disable());

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:63342"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization","Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
