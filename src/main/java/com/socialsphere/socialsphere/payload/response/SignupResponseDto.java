package com.socialsphere.socialsphere.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponseDto {
    private String message;
    private Boolean success;
    private JwtResponseDto jwtResponseDto;
}
