package com.socialsphere.socialsphere.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponseDto {
    private String message;
    private Boolean success;
}
