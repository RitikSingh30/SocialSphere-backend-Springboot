package com.socialsphere.socialsphere.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "Username should be present")
    private String userName;
    @NotBlank(message = "Password should be present")
    private String password;
}
