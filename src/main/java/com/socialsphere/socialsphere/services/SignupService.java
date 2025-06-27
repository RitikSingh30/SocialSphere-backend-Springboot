package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.SignupRequestDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;

public interface SignupService {
    SignupResponseDto signup(SignupRequestDto signupRequestDto);
}
