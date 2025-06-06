package com.socialsphere.socialsphere.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SendOtpResponseDto {
    private String message;
    private Boolean success;
}
