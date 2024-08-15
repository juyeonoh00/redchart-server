package server.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.http.server.reactive.ServerHttpResponseDecorator;

import org.springframework.stereotype.Component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    @Value("${jwt.secret}")
    private String secretKey;
    private final TokenService tokenService;
    private static final String HEADER_NAME = "user_id";
    private static final String USER_SERVICE_URI = "lb://user-service";




    private static final String[] EXCLUDED_PATHS = {"/", "/auth/login", "/member/signup", "/products","/api/posts/swagger-ui/**", "/api/posts/v3/api-docs/**",
            "/products/detail/**", "/member/find-password", "/member/reset-password", "/member/verify**",
            "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico"};


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info("filter");
        String path = exchange.getRequest().getURI().getPath();
        // EXCLUDED_PATHS에 포함된 경로는 JWT 검사를 건너뜁니다.
        if (Arrays.stream(EXCLUDED_PATHS).anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing or malformed");
        }


        String id = tokenService.validationToken(token, secretKey);
        if (id == null) {
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .uri(URI.create(USER_SERVICE_URI))
                    .build();
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();
            return chain.filter(modifiedExchange);
        }

        // id를 body에 추가
        String bodyContent = "{\"userId\":\"" + id + "\"}";

        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .build();

        return chain.filter(exchange.mutate()
                .request(modifiedRequest)
                .response(new ServerHttpResponseDecorator(exchange.getResponse()) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        ServerHttpResponse decoratedResponse = getDelegate();
                        decoratedResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                        DataBufferFactory bufferFactory = decoratedResponse.bufferFactory();
                        DataBuffer buffer = bufferFactory.wrap(bodyContent.getBytes(StandardCharsets.UTF_8));

                        return decoratedResponse.writeWith(Mono.just(buffer))
                                .then(super.writeWith(body));
                    }
                })
                .build());

    }
}



