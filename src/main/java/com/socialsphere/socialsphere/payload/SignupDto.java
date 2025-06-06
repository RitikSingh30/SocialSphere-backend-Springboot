package com.socialsphere.socialsphere.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
    @NotBlank(message = "Email should be present")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Name cannot be blank")
    private String fullName;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "UserName cannot be blank")
    private String userName;
    @NotBlank(message = "OTP cannot be blank")
    private String otp;
}
