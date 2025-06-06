package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.SignupDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;

public interface SignupService {
    SignupResponseDto signup(SignupDto signupDto);
}
