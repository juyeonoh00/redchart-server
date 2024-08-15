package server.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        System.out.println("SecurityFilterChain : 진입");
        return httpSecurity
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("api/kakao/**").permitAll()
                        .pathMatchers("/api/emails/**").permitAll()
                        .pathMatchers("/api/signin").permitAll()
                        .pathMatchers("/api/signup").permitAll()
                        .pathMatchers("/","/api/posts/swagger-ui/**", "/api/posts/v3/api-docs/**").permitAll()
                        .pathMatchers("/","/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
//                .exceptionHandling(ex->
//                    ex.accessDeniedHandler(new JwtAccessDeniedHandler())

    }

}


