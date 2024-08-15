package server.config.oauth2;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.config.jwt.JwtUtil;
import server.config.jwt.TokenDto;
import server.domain.User;
import server.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoLoginController {

    private final Oauth2TokenService oauth2TokenService;
    private final UserService memberService;
    private final JwtUtil jwtUtil;
    @GetMapping("/kakao")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code, HttpServletResponse response) {
        OauthToken oauthToken = oauth2TokenService.getKakaoAccessToken(code);
        OAuthAttributes oAuthAttributes = oauth2TokenService.loadKakao(oauthToken.getAccessToken(), oauthToken.getRefreshToken());
        User user = memberService.signUp(oAuthAttributes);
        TokenDto tokenDto = jwtUtil.generateToken(user, response);
        // member db에 저장
        // generator 호출
//        TokenDto tokenDto = jwtUtil.generateToken(authentication, response);
        return ResponseEntity.ok(tokenDto);
    }
}