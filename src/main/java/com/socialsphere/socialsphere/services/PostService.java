package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.dtos.CompletePostDetailDto;
import com.socialsphere.socialsphere.payload.dtos.CreatePostDto;
import com.socialsphere.socialsphere.payload.request.CreatePostRequestDto;

import java.util.List;

public interface PostService {
    CreatePostDto createPost(CreatePostRequestDto createPostRequestDto, String username);
    List<CompletePostDetailDto> getHomePagePost(String username);
}
