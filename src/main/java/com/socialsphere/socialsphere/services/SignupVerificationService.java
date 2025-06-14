package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.SignupVerificationDto;

import java.util.Map;

public interface SignupVerificationService {
    Map<String, Object> signupVerification(SignupVerificationDto signupVerificationDto);
}
