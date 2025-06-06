package com.socialsphere.socialsphere.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "Username should be present")
    private String userName;
    @NotBlank(message = "Password should be present")
    private String password;
}
