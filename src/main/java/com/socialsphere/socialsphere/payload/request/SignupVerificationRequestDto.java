package com.socialsphere.socialsphere.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupVerificationRequestDto {
    @NotBlank(message = "Email should be present")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Username should be present")
    private String userName;
}
