package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.payload.dtos.CompletePostDetailDto;
import com.socialsphere.socialsphere.payload.dtos.CreatePostDto;
import com.socialsphere.socialsphere.payload.request.CreatePostRequestDto;
import com.socialsphere.socialsphere.payload.response.ApiResponse;
import com.socialsphere.socialsphere.services.PostService;
import com.socialsphere.socialsphere.utility.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create-post/{emailId}")
    public ResponseEntity<ApiResponse<CreatePostDto>> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto, @PathVariable String emailId, HttpServletRequest request) {
      log.info("Create post journey started from Controller");
      CreatePostDto createdPostResponse = postService.createPost(createPostRequestDto,emailId);
      log.info("Create post journey completed from Controller");
      return new ResponseEntity<>(CommonUtil.getSuccessApiResponse("Post created successfully..!",createdPostResponse,request), HttpStatus.CREATED);
    }

    @GetMapping("/get-home-page-posts/{emailId}")
    public ResponseEntity<ApiResponse<List<CompletePostDetailDto>>> getHomePagePosts(@PathVariable String emailId, HttpServletRequest request){
        log.info("Get home page posts journey started from Controller");
        List<CompletePostDetailDto> completePostDetailDto = postService.getHomePagePost(emailId);
        log.info("Get home page posts journey completed from Controller");
        return ResponseEntity.ok(CommonUtil.getSuccessApiResponse("Posts retrieved successfully..!",completePostDetailDto,request));
    }
}
