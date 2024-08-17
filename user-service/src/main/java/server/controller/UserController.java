package server.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.config.jwt.JwtUtil;
import server.config.jwt.TokenDto;
import server.domain.User;
import server.dto.user.*;
import server.service.UserService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
        @PostMapping(value = "/signup")
        public ResponseEntity<SignUpResponseDto> singUp (@RequestBody SignUpDto signUpDto){
            User user = userService.signUp(signUpDto);
            SignUpResponseDto res = SignUpResponseDto.toDto(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

    @Operation(summary = "로그인", description = "로그인후, access/refresh Token 발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response) throws Exception {
        User user = userService.signIn(signInRequestDto);
        TokenDto tokenDto = jwtUtil.generateToken(user, response);
        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 코드 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email) throws NoSuchAlgorithmException, MessagingException, UnsupportedEncodingException {
        userService.sendCodeToEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저 이름 중복 확인", description = "username 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/user/check-username")
    public ResponseEntity checkUsername(@RequestParam("username") @Valid String username) {
        userService.checkDuplicatedUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "이메일 인증 코드 입력", description = "이메일로 전송된 코드 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        userService.verifiedCode(email, authCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/refresh-token")
    public String refresh(@RequestHeader("Authorization")String token, HttpServletResponse response) {
        String accessToken = jwtUtil.validateRefreshToken(token, response);

        return accessToken;
    }

//    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "202", description = "Accepted")
//    })
//    @PatchMapping("/user/update")
//    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequest request) {
//        UserDto updatedMember = userService.updateUser(principalDetails().getId(), request);
//        return ResponseEntity.ok(updatedMember);
//    }


}
