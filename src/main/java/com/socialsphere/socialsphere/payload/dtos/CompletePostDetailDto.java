package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompletePostDetailDto {
    private BasicUserInfoDto basicUserInfoDto;
    private List<PostDto> postDto;
}
