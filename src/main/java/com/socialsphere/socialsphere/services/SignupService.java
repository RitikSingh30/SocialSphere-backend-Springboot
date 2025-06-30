package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.request.SignupRequestDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;

public interface SignupService {
    SignupResponseDto signup(SignupRequestDto signupRequestDto);
}
