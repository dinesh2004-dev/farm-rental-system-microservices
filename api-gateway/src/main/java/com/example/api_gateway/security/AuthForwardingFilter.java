package com.example.api_gateway.security;

import com.example.api_gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthForwardingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthForwardingFilter.class);

    private final byte[] internalSecret;

    private final JwtUtil jwtUtil;


    public AuthForwardingFilter(@Value("${internal.auth.secret}") String internalSecretStr
    , JwtUtil jwtUtil) {
        this.internalSecret = internalSecretStr.getBytes(StandardCharsets.UTF_8);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public int getOrder() {
        // run after security has populated the Reactive SecurityContext
        return Ordered.LOWEST_PRECEDENCE - 10;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .flatMap(authentication -> {
                    if (authentication == null || !authentication.isAuthenticated()) {
                        log.debug("AuthForwardingFilter: no authenticated principal, continuing without mutating");
                        return chain.filter(exchange);
                    }



                    String token = (authentication.getCredentials() == null) ? null
                            : String.valueOf(authentication.getCredentials());


                    String userId = null;
                    try {
                        userId = String.valueOf(jwtUtil.extractUserId(token)); // extractUserId returns Integer in your util
                    } catch (Exception ex) {
                        log.debug("Failed to extract userId from token: {}", ex.getMessage());
                        // optionally continue without id or abort
                    }

                    String username = String.valueOf(authentication.getPrincipal());
                    List<String> roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                            .collect(Collectors.toList());


                    String payload = "user=" + username + ";roles=" + String.join(",", roles);
                    String signature = hmacBase64(payload, internalSecret);

                    log.debug("AuthForwardingFilter: user={}, roles={}, signature={}", username, roles, signature);

                    var mutated = exchange.getRequest().mutate()
                            .header("X-Authenticated-User", username)
                            .header("X-Authenticated-UserId", userId == null ? "" : userId)
                            .header("X-Authenticated-Roles", String.join(",", roles))
                            .header("X-Auth-Signature", signature)
                            .headers(h -> h.remove(HttpHeaders.AUTHORIZATION))
                            .build();

                    return chain.filter(exchange.mutate().request(mutated).build());
                })
                // if SecurityContext is absent, just continue the filter chain without mutation
                .switchIfEmpty(chain.filter(exchange));
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
}
