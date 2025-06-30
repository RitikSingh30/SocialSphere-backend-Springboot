package com.socialsphere.socialsphere.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmRequestDto {
    @NotBlank(message = "Token should be present")
    private String token;
    @NotBlank(message = "New password should be present")
    private String newPassword;
}
