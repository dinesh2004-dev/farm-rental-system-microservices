package com.example.user_service.util;

import com.example.user_service.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // 1. Read the shared secret from your properties file
    // This MUST be the same secret as your API Gateway
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    // 2. Create a stable, reusable secret key from the string
    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- THIS IS THE SIGNATURE CHANGE ---
    // 3. Change the method to accept the full UserDetails object
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        // Put roles as a List<String>, not a single CSV string
        var rolesList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // yields "ROLE_USER", etc.
                .collect(Collectors.toList());



        claims.put("roles", rolesList);
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // You don't need the validation/extraction methods in this service,
    // only the API gateway needs those.
}