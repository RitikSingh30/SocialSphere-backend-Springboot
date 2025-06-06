package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.payload.LoginDto;
import com.socialsphere.socialsphere.payload.SignupDto;
import com.socialsphere.socialsphere.payload.response.LoginResponseDto;
import com.socialsphere.socialsphere.payload.response.SendOtpResponseDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;
import com.socialsphere.socialsphere.security.JwtUtil;
import com.socialsphere.socialsphere.services.LoginService;
import com.socialsphere.socialsphere.services.RefreshTokenService;
import com.socialsphere.socialsphere.services.SendOtpService;
import com.socialsphere.socialsphere.services.SignupService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final SendOtpService sendOtpService;
    private final SignupService signupService;
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupDto signupDto, HttpServletResponse response) {
        log.info("Signup Journey Started from Controller");
        SignupResponseDto signupResponseDto = signupService.signup(signupDto);

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(signupDto.getUserName());

        // Creating JWT token and setting it in cookie
        log.info("Generating JWT token");
        String accessToken = jwtUtil.generateToken(signupDto.getUserName());
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                        .httpOnly(true)
                                .secure(true)
                                        .path("/")
                                                .sameSite("Strict")
                                                        .maxAge(cookieExpiry)
                                                                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return JwtResponseDto.builder()
                        .accessToken(accessToken)
                                .token(refreshToken.getToken())
                                        .build();

        log.info("Signup Journey Completed from Controller");
        return new ResponseEntity<>(signupResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        log.info("Login Journey Started from Controller");
        LoginResponseDto loginResponseDto = loginService.login(loginDto);
        log.info("Login Journey Completed from Controller");
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/sendOtp/{emailId}")
    public ResponseEntity<SendOtpResponseDto> sendOtp(@PathVariable String emailId){
        log.info("Sending OTP for email id journey started from Controller");
        sendOtpService.sendOtp(emailId);
        log.info("Sending OTP for email id journey completed from Controller");
        return ResponseEntity.ok().body(new SendOtpResponseDto("Otp sent successfully", true));
    }
}
