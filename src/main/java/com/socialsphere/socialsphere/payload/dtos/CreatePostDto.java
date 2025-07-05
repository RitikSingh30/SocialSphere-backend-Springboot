package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostDto {
    private String type; // image/video
    private String url;
}
