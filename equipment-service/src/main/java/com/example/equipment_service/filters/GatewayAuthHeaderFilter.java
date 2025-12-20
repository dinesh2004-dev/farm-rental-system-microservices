package com.example.equipment_service.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayAuthHeaderFilter implements Filter {

    @Value("${internal.auth.secret}")
    private String internalSecret;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String user = req.getHeader("X-Authenticated-User");
        String rolesHeader = req.getHeader("X-Authenticated-Roles");
        String signature = req.getHeader("X-Auth-Signature");
        String userId = req.getHeader("X-Authenticated-UserId");

        if (user != null && signature != null) {
            String payload = "user=" + user + ";roles=" + (rolesHeader == null ? "" : rolesHeader);
            String expected = hmacBase64(payload, internalSecret.getBytes(StandardCharsets.UTF_8));
            if (constantTimeEquals(expected, signature)) {
                List<String> roles = rolesHeader == null
                        ? List.of()
                        : Arrays.stream(rolesHeader.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                var authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority(r.startsWith("ROLE_") ? r : "ROLE_" + r))
                        .collect(Collectors.toList());

                var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                Map<String, Object> details = new HashMap<>();
                details.put("userId", userId);
                auth.setDetails(details);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private String hmacBase64(String data, byte[] secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(raw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] aa = a.getBytes(StandardCharsets.UTF_8);
        byte[] bb = b.getBytes(StandardCharsets.UTF_8);
        if (aa.length != bb.length) return false;
        int result = 0;
        for (int i = 0; i < aa.length; i++) result |= aa[i] ^ bb[i];
        return result == 0;
    }
}
