package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.SignupVerificationRequestDto;

import java.util.Map;

public interface SignupVerificationService {
    Map<String, Object> signupVerification(SignupVerificationRequestDto signupVerificationRequestDto);
}
