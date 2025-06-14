package com.socialsphere.socialsphere.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponseDto {
    private String username;
    private String email;
    private String fullName;
    private JwtResponseDto jwtResponseDto;
}
