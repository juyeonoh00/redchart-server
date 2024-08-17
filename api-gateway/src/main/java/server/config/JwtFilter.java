package server.config;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
class JwtFilter implements WebFilter {
    @Value("${jwt.secret}")
    private String secretKey;
    private final TokenService tokenService;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
    private static final String USER_SERVICE_URI = "lb://user-service";

    private static final String[] EXCLUDED_PATHS = {
            "api/users/kakao/**",
            "/api/users/emails/**",
            "/api/users/signin",
            "/api/users/signup",
            "/api/posts/swagger-ui/**",
            "/api/posts/v3/api-docs/**",
            "/api/users/swagger-ui/**",
            "/api/users/v3/api-docs/**",
            "/",
            "/swagger-ui/**",
            "/v3/api-docs/**"};
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String excludedPath : EXCLUDED_PATHS) {
            if (pathMatcher.match(excludedPath, requestPath)) {
                log.info("Path {} is excluded from JWT filtering", requestPath);
                return chain.filter(exchange);
            }
        }
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .method(HttpMethod.POST)
                    .uri(URI.create("http://localhost:8082/api/users/signin"))
                    .build();
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();
            return chain.filter(modifiedExchange);
        }
        return validateAndProcessToken(exchange, chain, token);
    }
    private Mono<Void> validateAndProcessToken(ServerWebExchange exchange, WebFilterChain chain, String token) {
        try {
            String id = tokenService.validationToken(token, secretKey);
            if (id == null) {
                return chain.filter(exchange);
            }
            return processValidToken(exchange, chain, id);
        } catch (ExpiredJwtException e) {
            log.info("Invalid JWT Token", e);
            return handleExpiredToken(exchange, chain);
        } catch (Exception e) {
            log.info("Jwt Error", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
    private Mono<Void> processValidToken(ServerWebExchange exchange, WebFilterChain chain, String id) {
        log.info(id);
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("userId", id)
                .build();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                new User(id, "", new ArrayList<>()), null, new ArrayList<>()
        );
        return chain.filter(exchange.mutate().request(modifiedRequest).build())
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }
    private Mono<Void> handleExpiredToken(ServerWebExchange exchange, WebFilterChain chain) {
        return WebClient.builder()
                .filter(lbFunction)
                .build().get()
                .uri(URI.create(USER_SERVICE_URI+"/api/users/refresh-token"))
                .header(HttpHeaders.AUTHORIZATION, exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(newToken -> {
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                            .build();
                    ServerWebExchange modifiedExchange = exchange.mutate()
                            .request(modifiedRequest)
                            .build();
                    return validateAndProcessToken(modifiedExchange, chain, "Bearer " + newToken);
                })
                .onErrorResume(e -> {
                    log.error("Error refreshing token", e);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}