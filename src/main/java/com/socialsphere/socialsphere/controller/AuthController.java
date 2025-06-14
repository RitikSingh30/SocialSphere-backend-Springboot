package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.constant.CommonConstant;
import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.exception.TokenException;
import com.socialsphere.socialsphere.helper.ValidateOtpHelper;
import com.socialsphere.socialsphere.payload.*;
import com.socialsphere.socialsphere.payload.response.*;
import com.socialsphere.socialsphere.security.JwtUtil;
import com.socialsphere.socialsphere.services.*;
import com.socialsphere.socialsphere.utility.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final SendOtpService sendOtpService;
    private final SignupService signupService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final ForgotPasswordService forgotPasswordService;
    private final ValidateOtpHelper validateOtpHelper;
    private final SignupVerificationService signupVerificationService;

    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;

    @PostMapping("/signupVerification")
    public ResponseEntity<ApiResponse<Map<String,Object>>> signupVerification(@RequestBody SignupVerificationDto signupVerificationDto) {
        log.info("Signup verification journey Started from Controller");
        Map<String,Object> data = signupVerificationService.signupVerification(signupVerificationDto);
        Map<String,Object> metadata = Map.of("timestamp", Instant.now().toString());
        ApiResponse<Map<String,Object>> apiResponse = ResponseUtil.success("Otp send successful",data,metadata);
        log.info("Signup verification journey Completed from Controller");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupDto signupDto, HttpServletResponse response) {
        log.info("Signup Journey Started from Controller");
        SignupResponseDto signupResponseDto = signupService.signup(signupDto);
        // Creating the jwt token, cookies and refreshToken
        JwtResponseDto jwtResponseDto = getJwtResponseDto(signupDto.getUserName().toLowerCase(), response);
        signupResponseDto.setJwtResponseDto(jwtResponseDto);
        ApiResponse<SignupResponseDto> apiResponse = ResponseUtil.success("Signup successful",signupResponseDto,null);
        log.info("Signup Journey Completed from Controller");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response){
        log.info("Login Journey Started from Controller");
        // Authenticating the user manually.
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName().toLowerCase(),loginDto.getPassword()));
        // Checking if the user is authenticated or not
        if(authentication.isAuthenticated()){
            JwtResponseDto jwtResponseDto = getJwtResponseDto(loginDto.getUserName().toLowerCase(), response);
            LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                    .message("Login successful..!!")
                    .success(true)
                    .jwtResponseDto(jwtResponseDto)
                    .build();
            log.info("Login Journey Completed from Controller");
            return ResponseEntity.ok(loginResponseDto);
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }

    @PostMapping("/forgotPassword/{emailId}")
    public ResponseEntity<SendOtpResponseDto> forgetPassword(@PathVariable String emailId){
        log.info("Forgot Password Journey Started from Controller");
        forgotPasswordService.forgotPassword(emailId);
        log.info("Forgot Password Journey Started from Controller");
        return new ResponseEntity<>(new SendOtpResponseDto("Otp sent successfully", true), HttpStatus.CREATED);
    }

    @PostMapping("/sendOtp/{emailId}")
    public ResponseEntity<SendOtpResponseDto> sendOtp(@PathVariable String emailId){
        log.info("Sending OTP for email id journey started from Controller");
        sendOtpService.sendOtp(emailId);
        log.info("Sending OTP for email id journey completed from Controller");
        return ResponseEntity.ok().body(new SendOtpResponseDto("Otp sent successfully", true));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<VerifyOtpResponseDto> verifyOtp(@RequestBody @Valid VerifyOtpDto verifyOtpDto){
        log.info("Verifying OTP for email id journey started from Controller");
        validateOtpHelper.isOtpValid(verifyOtpDto.getOtp(), verifyOtpDto.getEmail());
        log.info("Verifying OTP for email id journey completed from Controller");
        return ResponseEntity.ok(new VerifyOtpResponseDto("Otp verified successfully", true));
    }

    @PostMapping("/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenDto refreshTokenRequestDto){
        log.info("Refreshing the refresh token in refreshToken endpoint");
        try{
            return refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshTokenEntity::getUserEntity)
                    .map(userEntity -> {
                        String accessToken = jwtUtil.generateToken(userEntity.getUsername());
                        return JwtResponseDto.builder()
                                .accessToken(accessToken)
                                .token(refreshTokenRequestDto.getToken())
                                .build();
                    }).orElseThrow(() -> new TokenException("Refresh token not found"));
        } catch (Exception e){
            log.error("Error occurred while refreshing token", e);
            throw e ;
        }
    }

    private JwtResponseDto getJwtResponseDto(String userName, HttpServletResponse response){
        try{
            log.info("Entering getJwtResponseDto to generate JwtToken, Cookie and Refresh Token.");
            // Creating refresh token
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userName);
            // Creating JWT token and setting it in cookie
            String accessToken = jwtUtil.generateToken(userName);
            // Setting JWT token in the cookie
            ResponseCookie cookie = ResponseCookie.from(CommonConstant.ACCESS_TOKEN, accessToken)
                    .httpOnly(true)
                    .secure(true) // Todo need to check whether this works on http or not
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(cookieExpiry)
                    .build();
            // Adding cookie into the header
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            log.info("Exiting getJwtResponseDto token created.");
            return JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken())
                    .build();
        } catch (Exception e){
            log.error("Error occurred in getJwtResponseDto method in controller", e);
            throw e;
        }
    }
}
