package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.entity.ResetTokenEntity;
import com.socialsphere.socialsphere.exception.TokenException;
import com.socialsphere.socialsphere.helper.ValidateOtpHelper;
import com.socialsphere.socialsphere.payload.*;
import com.socialsphere.socialsphere.payload.response.*;
import com.socialsphere.socialsphere.repository.ResetTokenRepo;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static com.socialsphere.socialsphere.utility.CommonUtil.getErrorApiResponse;
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
    private final PasswordResetService passwordResetService;
    private final ResetTokenRepo resetTokenRepo;

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

    @PostMapping("/signup-verification")
    public ResponseEntity<ApiResponse<Map<String,Object>>> signupVerification(@RequestBody SignupVerificationRequestDto signupVerificationRequestDto, HttpServletRequest request) {
        log.info("Signup verification journey Started from Controller");
        Map<String,Object> data = signupVerificationService.signupVerification(signupVerificationRequestDto);
        log.info("Signup verification journey Completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("Otp send successful",data,request));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response, HttpServletRequest request) {
        log.info("Signup Journey Started from Controller");
        SignupResponseDto signupResponseDto = signupService.signup(signupRequestDto);
        // Creating the jwt token, cookies and refreshToken
        JwtResponseDto jwtResponseDto = getJwtResponseDto(signupRequestDto.getUserName().toLowerCase(), response);
        signupResponseDto.setJwtResponseDto(jwtResponseDto);
        URI location = URI.create(request.getRequestURI());
        log.info("Signup Journey Completed from Controller");
        return ResponseEntity.created(location).body(getSuccessApiResponse("Signup successful",signupResponseDto,request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response, HttpServletRequest request){
        log.info("Login Journey Started from Controller");
        // Authenticating the user manually.
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUserName().toLowerCase(),loginRequestDto.getPassword()));
        // Checking if the user is authenticated or not
        if(authentication.isAuthenticated()){
            JwtResponseDto jwtResponseDto = getJwtResponseDto(loginRequestDto.getUserName().toLowerCase(), response);
            LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                    .jwtResponseDto(jwtResponseDto)
                    .build();
            log.info("Login Journey Completed from Controller");
            return ResponseEntity.ok().body(getSuccessApiResponse("Login successful..!!",loginResponseDto,request));
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }

    @PostMapping("/forgot-password/{emailId}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> forgetPassword(@PathVariable String emailId, HttpServletRequest request) {
        log.info("Forgot Password Journey Started from Controller");
        Map<String, Object> data = forgotPasswordService.forgotPassword(emailId);
        URI location = URI.create(request.getRequestURI());
        log.info("Forgot Password Journey completed from Controller");
        return ResponseEntity.created(location).body(getSuccessApiResponse("Otp send successfully",data,request));
    }

    @PostMapping("/send-otp/{emailId}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> sendOtp(@PathVariable String emailId, HttpServletRequest request) {
        log.info("Sending OTP for email id journey started from Controller");
        String otp = sendOtpService.sendOtp(emailId);
        Map<String,Object> data = CommonUtil.prepareOtpDataResponse(otp,emailId);
        log.info("Sending OTP for email id journey completed from Controller");
        return ResponseEntity.ok().body(getSuccessApiResponse("Otp send successfully",data,request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Map<String,Object>>> verifyOtp(@RequestBody @Valid VerifyOtpRequestDto verifyOtpRequestDto, HttpServletRequest request) {
        log.info("Verifying OTP for email id journey started from Controller");
        Map<String,Object> data = validateOtpHelper.isOtpValid(verifyOtpRequestDto.getOtp(), verifyOtpRequestDto.getEmail());
        log.info("Verifying OTP for email id journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("Otp verified successfully",data,request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> requestResetPassword(@RequestBody @Valid PasswordResetRequestDto passwordResetRequestDto, HttpServletRequest request) {
        log.info("Reset Password Journey Started from Controller");
        passwordResetService.resetPassword(passwordResetRequestDto.getEmail());
        log.info("Reset Password Journey Completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("If the email is registered, you'll get a reset link",null,request));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> validateResetPasswordToken(@RequestParam("token") String token, HttpServletRequest request) {
        log.info("Validate reset Password Journey Started from Controller");
        Optional<ResetTokenEntity> tokenOpt = resetTokenRepo.findByToken(token);

        // check if the token is valid or not
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorApiResponse("Invalid or expired token",null));
        }
        log.info("Validate reset Password Journey Completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("Token is valid", null,request));
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody PasswordResetConfirmRequestDto passwordResetConfirmRequestDto, HttpServletRequest request) {
        log.info("Confirm reset Password Journey Started from Controller");
        Optional<ResetTokenEntity> tokenOpt = resetTokenRepo.findByToken(passwordResetConfirmRequestDto.getToken());

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorApiResponse("Token is invalid or expired",null));
        }

        passwordResetService.confirmResetPassword(tokenOpt.get(),passwordResetConfirmRequestDto);
        log.info("Confirm reset Password Journey Completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("Password updated",null,request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<JwtResponseDto>> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto, HttpServletRequest request) {
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
