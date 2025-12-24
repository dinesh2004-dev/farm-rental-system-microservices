package com.example.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret; // either raw secret string or base64-encoded bytes

    private Key key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret must be set");
        }

        // try to decode as base64, otherwise use raw UTF-8 bytes
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secret);
            // simple check: decoded value should be >= 32 bytes for HS256
            if (keyBytes.length < 32) {
                // fall back to raw bytes if base64 decode produced too-short key
                keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            }
        } catch (IllegalArgumentException ex) {
            // not base64 -> use raw bytes
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        // ensure key has sufficient length (HS256 needs 256 bits / 32 bytes)
        if (keyBytes.length < 32) {
            throw new IllegalStateException("jwt.secret is too short; must be at least 32 bytes (256 bits)");
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaims(token,c -> c.get("email",String.class));
    }
    public int extractUserId(String token) {
        return extractClaims(token, c -> Integer.parseInt(c.getSubject()));
    }
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        Map<String, Object> claims = extraClaims != null ? extraClaims : new HashMap<>();
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * 60 * 60 * 10)) // 10 hrs
                .signWith(key)
                .compact();
    }

    public String generateToken(String username) {
        return generateToken(username, null);
    }

    public boolean validateToken(String token, String username) {
        final String exactUsername = extractUsername(token);
        return (exactUsername.equals(username) && !isTokenExpired(token));
    }
}
