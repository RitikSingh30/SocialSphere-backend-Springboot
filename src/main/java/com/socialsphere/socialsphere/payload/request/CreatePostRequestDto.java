package com.socialsphere.socialsphere.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostRequestDto {
    @NotNull(message = "caption is required")
    private String caption;
    @NotNull(message = "post type is required")
    private String type; // image/video
    @NotNull(message = "post url is required")
    private String url;
}
