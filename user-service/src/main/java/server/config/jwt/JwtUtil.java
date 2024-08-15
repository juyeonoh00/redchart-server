package server.config.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import server.config.jwt.exception.ErrorCode;
import server.config.jwt.exception.TokenExcption;
import server.domain.User;
import server.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public String createAccessToken(User user, HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(user.getUsername());
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, user.getAuthority())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        return accessToken;
    }
    public TokenDto generateToken(User user, HttpServletResponse response){
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        // 권한들
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, user.getAuthority())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .claim(JwtProperties.AUTHORITIES_KEY, user.getAuthority())
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
                .compact();
        response.addHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + accessToken);
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken, accessToken));
        return new TokenDto(accessToken, refreshToken);
    }


    // 에러 처리 세분화 필요
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException | TokenExcption e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public void validateRefreshToken(String token, HttpServletResponse response) throws IOException {
        log.error("💡 Access Token 이 만료되었습니다.");
        try
        {
            RefreshToken refreshTokenInfo = refreshTokenRepository.findByAccessToken(token).orElseThrow(()->new TokenExcption(ErrorCode.TOKEN_NOT_FOUND));
            String refreshToken = refreshTokenInfo.getRefreshToken();
            validateToken(refreshToken);
            Long memberId = refreshTokenInfo.getMemberId();
            User user = userRepository.findById(memberId)
                    .orElseThrow(() -> new TokenExcption(ErrorCode.USER_NOT_FOUNDED));
            // 리프레시 토큰 정보 기반 에세스 토큰 재발급
            String accessToken = createAccessToken(user, response);
            // 레디스에 저장
            refreshTokenRepository.save(new RefreshToken(memberId, refreshToken, accessToken));
        }catch (TokenExcption e) {
            // 리프레시 토큰 없으면
            throw e;
        }
    }
}
