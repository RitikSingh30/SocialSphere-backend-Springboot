package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private BasicUserInfoDto basicUserInfoDto;
    private String content;
}
