package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicUserInfoDto {
    private String username;
    private String profilePicture;
}
