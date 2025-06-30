package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.request.SignupVerificationRequestDto;

import java.util.Map;

public interface SignupVerificationService {
    Map<String, Object> signupVerification(SignupVerificationRequestDto signupVerificationRequestDto);
}
