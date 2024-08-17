package server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.config.email.EmailService;
import server.config.jwt.JwtUtil;
import server.config.oauth2.OAuthAttributes;
import server.config.redis.RedisService;
import server.domain.Authority;
import server.domain.User;

import server.dto.user.*;
import server.exception.EmailAlreadyExistsException;
import server.exception.UsernameAlreadyExistsException;
import server.feignclient.ClientFollowersDto;
import server.feignclient.ClientUserDto;
import server.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public User signUp(SignUpDto signUpDto) {
        signUpDto.setAuthority(Authority.valueOf("ROLE_USER"));
        User user = signUpDto.toEntity();
        user.passwordEncoding(passwordEncoder);
        userRepository.save(user);
        return user;
    }

    // 소셜 로그인
    @Transactional
    public User signUp(OAuthAttributes oAuthAttributes) {
//        oAuthAttributes.setAuthority(Authority.valueOf("ROLE_USER"));
        User user = oAuthAttributes.toEntity();
        userRepository.save(user);
        return user;
    }


    public void sendCodeToEmail(String toEmail) throws NoSuchAlgorithmException, MessagingException, UnsupportedEncodingException, jakarta.mail.MessagingException {
        this.checkDuplicatedEmail(toEmail);
        String title = "[ODIRO] 이메일 인증 번호";
        String authCode = this.createCode();
        emailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    public void verifiedCode(String email, String authCode) {
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        if (!authResult) {
            log.debug("UserService.createCode() exception occur");
        }
    }

    private void checkDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new EmailAlreadyExistsException(email);

        }
    }
    public void checkDuplicatedUsername(String username) {
        Optional<User> user = userRepository.findByusername(username);
        if (user.isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }
    }
    private String createCode() throws NoSuchAlgorithmException {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("userService.createCode() exception occur");
            throw e;
        }
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
            // 중복 확인
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
            // 이메일 검증
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
            // 비밀번호
        }
        userRepository.save(user);

        // 이메일 변경 시 이메일 인증이 필요함
        // 이메일 중복도 확인해야함
        // 비밀번호 암호화해야함
        return new UserDto(user);
    }

    public ClientUserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new ClientUserDto(user);
    }
    public User signIn(SignInRequestDto signInRequestDto) throws Exception {
        User user = userRepository.findByusername(signInRequestDto.getUsername()).orElseThrow(()-> new
                RuntimeException("user가 존재하지 않습니다."));
        if (user == null || !passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            // 비밀번호가 일치하지 않거나 사용자가 존재하지 않으면 예외 발생
            throw new Exception("Invalid username or password.");
        }
        return user;
    }

}