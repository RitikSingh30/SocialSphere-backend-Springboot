package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.dtos.CreatePostDto;
import com.socialsphere.socialsphere.payload.request.CreatePostRequestDto;

public interface PostService {
    CreatePostDto createPost(CreatePostRequestDto createPostRequestDto, String emailId);
}
