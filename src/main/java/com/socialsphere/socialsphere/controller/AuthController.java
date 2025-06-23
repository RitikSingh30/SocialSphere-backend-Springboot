package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.exception.TokenException;
import com.socialsphere.socialsphere.helper.ValidateOtpHelper;
import com.socialsphere.socialsphere.payload.*;
import com.socialsphere.socialsphere.payload.response.*;
import com.socialsphere.socialsphere.security.JwtUtil;
import com.socialsphere.socialsphere.services.*;
import com.socialsphere.socialsphere.utility.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.socialsphere.socialsphere.utility.CommonUtil.getSuccessApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final SendOtpService sendOtpService;
    private final SignupService signupService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final ForgotPasswordService forgotPasswordService;
    private final ValidateOtpHelper validateOtpHelper;
    private final SignupVerificationService signupVerificationService;

    @Value("${jwt.cookie.expiry}")
    private int cookieExpiry;

    @Value("${jwt.cookie.secure}")
    private boolean isSecure;

    @Value("${jwt.cookie.name}")
    private String cookieName;

    @Value("${jwt.cookie.path}")
    private String cookiePath;
    @Value("${jwt.cookie.sameSite}")
    private String cookieSameSite;

    @PostMapping("/signupVerification")
    public ResponseEntity<ApiResponse<Map<String,Object>>> signupVerification(@RequestBody SignupVerificationDto signupVerificationDto, HttpServletRequest request) {
        log.info("Signup verification journey Started from Controller");
        Map<String,Object> data = signupVerificationService.signupVerification(signupVerificationDto);
        log.info("Signup verification journey Completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("Otp send successful",data,request));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupDto signupDto, HttpServletResponse response, HttpServletRequest request) {
        log.info("Signup Journey Started from Controller");
        SignupResponseDto signupResponseDto = signupService.signup(signupDto);
        // Creating the jwt token, cookies and refreshToken
        JwtResponseDto jwtResponseDto = getJwtResponseDto(signupDto.getUserName().toLowerCase(), response);
        signupResponseDto.setJwtResponseDto(jwtResponseDto);
        URI location = URI.create(request.getRequestURI());
        log.info("Signup Journey Completed from Controller");
        return ResponseEntity.created(location).body(getSuccessApiResponse("Signup successful",signupResponseDto,request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request){
        log.info("Login Journey Started from Controller");
        // Authenticating the user manually.
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName().toLowerCase(),loginDto.getPassword()));
        // Checking if the user is authenticated or not
        if(authentication.isAuthenticated()){
            JwtResponseDto jwtResponseDto = getJwtResponseDto(loginDto.getUserName().toLowerCase(), response);
            LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                    .jwtResponseDto(jwtResponseDto)
                    .build();
            log.info("Login Journey Completed from Controller");
            return ResponseEntity.ok().body(getSuccessApiResponse("Login successful..!!",loginResponseDto,request));
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }

    @PostMapping("/forgotPassword/{emailId}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> forgetPassword(@PathVariable String emailId, HttpServletRequest request) {
        log.info("Forgot Password Journey Started from Controller");
        Map<String, Object> data = forgotPasswordService.forgotPassword(emailId);
        URI location = URI.create(request.getRequestURI());
        log.info("Forgot Password Journey completed from Controller");
        return ResponseEntity.created(location).body(getSuccessApiResponse("Otp send successfully",data,request));
    }

    @PostMapping("/sendOtp/{emailId}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> sendOtp(@PathVariable String emailId, HttpServletRequest request) {
        log.info("Sending OTP for email id journey started from Controller");
        String otp = sendOtpService.sendOtp(emailId);
        Map<String,Object> data = CommonUtil.prepareOtpDataResponse(otp,emailId);
        log.info("Sending OTP for email id journey completed from Controller");
        return ResponseEntity.ok().body(getSuccessApiResponse("Otp send successfully",data,request));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<Map<String,Object>>> verifyOtp(@RequestBody @Valid VerifyOtpDto verifyOtpDto, HttpServletRequest request) {
        log.info("Verifying OTP for email id journey started from Controller");
        Map<String,Object> data = validateOtpHelper.isOtpValid(verifyOtpDto.getOtp(), verifyOtpDto.getEmail());
        log.info("Verifying OTP for email id journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("Otp verified successfully",data,request));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<JwtResponseDto>> refreshToken(@RequestBody RefreshTokenDto refreshTokenRequestDto, HttpServletRequest request) {
        log.info("Refreshing the refresh token in refreshToken endpoint");
        try{
            JwtResponseDto jwtResponseDto = refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshTokenEntity::getUserEntity)
                    .map(userEntity -> {
                        String accessToken = jwtUtil.generateToken(userEntity.getUsername());
                        return JwtResponseDto.builder()
                                .accessToken(accessToken)
                                .token(refreshTokenRequestDto.getToken())
                                .build();
                    }).orElseThrow(() -> new TokenException("Refresh token not found"));
            return ResponseEntity.ok().body(getSuccessApiResponse("Refresh token created",jwtResponseDto,request));
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
            ResponseCookie cookie = createJwtCookie(accessToken);
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

    private ResponseCookie createJwtCookie(String accessToken) {
        return ResponseCookie.from(cookieName, accessToken)
                .httpOnly(true)
                .secure(isSecure) // Todo need to check whether this works on http or not
                .path(cookiePath)
                .sameSite(cookieSameSite)
                .maxAge(cookieExpiry)
                .build();
    }

}
