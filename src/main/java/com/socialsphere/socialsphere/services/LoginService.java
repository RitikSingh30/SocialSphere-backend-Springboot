package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.LoginDto;
import com.socialsphere.socialsphere.payload.response.LoginResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginDto loginDto);
}
