package com.socialsphere.socialsphere.services;

import java.util.Map;

public interface ForgotPasswordService {
    Map<String, Object> forgotPassword(String emailId);
}
