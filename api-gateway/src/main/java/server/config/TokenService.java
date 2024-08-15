package server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {
    public String validationToken(String token, String secretKey){
        token = token.substring(7).trim();
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token).getBody();
            return claims.get("id", String.class);
        }catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            return null;
        }
    }

}
