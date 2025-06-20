package com.socialsphere.socialsphere.payload.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UnauthorizedResponseDto {
    private String message;
    private Boolean success;
}
