package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto {
    private String url;
    private String caption;
    private String type;
    private List<BasicUserInfoDto> like;
    private List<CommentDto> commentDto;
}
