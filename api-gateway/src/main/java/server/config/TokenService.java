package server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {

    public String validationToken(String token, String secretKey){
        token = token.substring(7).trim();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token).getBody();
            // 권한 필요 시 추가
//            Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get("auth").toString()));
        return claims.get("sub", String.class);
    }

}
