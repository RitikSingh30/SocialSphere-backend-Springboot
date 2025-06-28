package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.entity.ResetTokenEntity;
import com.socialsphere.socialsphere.payload.PasswordResetConfirmRequestDto;

public interface PasswordResetService {
    void resetPassword(String email);
    void confirmResetPassword(ResetTokenEntity tokenRecord, PasswordResetConfirmRequestDto passwordResetConfirmRequestDto);
}
