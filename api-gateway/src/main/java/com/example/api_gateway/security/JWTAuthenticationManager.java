package com.example.api_gateway.security;

import com.example.api_gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationManager.class);

    public JWTAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.debug("authenticate() called with authentication={}", authentication);

        String token = (authentication == null) ? null : (String) authentication.getCredentials();
        if (token == null) {
            // no token present — nothing to authenticate; let other filters decide
            return Mono.empty();
        }

        try {
            String username = jwtUtil.extractUsername(token);
            if (username == null) {
                log.debug("JWT has no subject");
                return Mono.error(new BadCredentialsException("Invalid JWT: no subject"));
            }

            if (!jwtUtil.validateToken(token, username)) {
                log.debug("JWT validation failed for user {}", username);
                return Mono.error(new BadCredentialsException("Invalid or expired JWT"));
            }

            List<String> roles = jwtUtil.extractClaims(token, claims -> claims.get("roles", List.class));
            var authorities = (roles == null)
                    ? List.<SimpleGrantedAuthority>of()
                    : roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, token, authorities);

            return Mono.just(auth);

        } catch (Exception ex) {
            // log the actual parsing/signature error for debugging and return failure
            log.debug("JWT validation error: {}", ex.getMessage(), ex);
            return Mono.error(new BadCredentialsException("Invalid JWT", ex));
        }
    }
}
