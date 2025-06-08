package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.constant.CommonConstant;
import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.payload.SignupDto;
import com.socialsphere.socialsphere.payload.response.JwtResponseDto;
import com.socialsphere.socialsphere.payload.response.SendOtpResponseDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;
import com.socialsphere.socialsphere.security.JwtUtil;
import com.socialsphere.socialsphere.services.RefreshTokenService;
import com.socialsphere.socialsphere.services.SendOtpService;
import com.socialsphere.socialsphere.services.SignupService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    AuthController authController;
    @Mock
    SendOtpService sendOtpService;
    @Mock
    SignupService signupService;
    @Mock
    RefreshTokenService refreshTokenService;
    @Mock
    JwtUtil jwtUtil;

    @Test
    void testSignUp(){
        // Initialize
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        SignupDto signupDto = SignupDto.builder()
                .userName("ritikSingh")
                .build();
        // Mock
        when(signupService.signup(any(SignupDto.class))).thenReturn(new SignupResponseDto());
        when(refreshTokenService.createRefreshToken(anyString())).thenReturn(getRefreshTokenEntity());
        when(jwtUtil.generateToken(anyString())).thenReturn(CommonConstant.ACCESS_TOKEN);
        // Act and verify
        ResponseEntity<SignupResponseDto> signupResponse = authController.signup(signupDto, httpServletResponse);
        assertNotNull(signupResponse);
        assertEquals(HttpStatus.CREATED, signupResponse.getStatusCode());
        assertNotNull(signupResponse.getBody());
        assertEquals(getJwtResponseDto(),signupResponse.getBody().getJwtResponseDto());
        verify(httpServletResponse).addHeader(eq(HttpHeaders.SET_COOKIE),anyString());
    }

    @Test
    void testSendOtp(){
        doNothing().when(sendOtpService).sendOtp(anyString());
        ResponseEntity<SendOtpResponseDto> sendOtpResponse = authController.sendOtp(CommonConstant.EMAIL_ID);
        assertNotNull(sendOtpResponse);
        assertEquals(HttpStatus.OK, sendOtpResponse.getStatusCode());
        assertNotNull(sendOtpResponse.getBody());
        assertEquals("Otp sent successfully", sendOtpResponse.getBody().getMessage());
        assertTrue(sendOtpResponse.getBody().getSuccess());
    }

    private JwtResponseDto getJwtResponseDto(){
        return JwtResponseDto.builder()
                .accessToken(CommonConstant.ACCESS_TOKEN)
                .token(CommonConstant.REFRESH_TOKEN)
                .build();
    }

    private RefreshTokenEntity getRefreshTokenEntity(){
        return RefreshTokenEntity.builder()
                .token(CommonConstant.REFRESH_TOKEN)
                .build();
    }

}