package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.PostEntity;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.ApiException;
import com.socialsphere.socialsphere.exception.UserDoesNotExistException;
import com.socialsphere.socialsphere.helper.CloudinaryHelper;
import com.socialsphere.socialsphere.payload.dtos.CreatePostDto;
import com.socialsphere.socialsphere.payload.request.CreatePostRequestDto;
import com.socialsphere.socialsphere.repository.PostRepo;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final CloudinaryHelper cloudinaryHelper;
    private final PostRepo postRepo;
    private final UserRepo userRepo;

    @Transactional
    @Override
    public CreatePostDto createPost(CreatePostRequestDto createPostRequestDto, String emailId) {
        log.info("Starting post creation for user with email: {}", emailId);
        try{
            // save media to cloudinary
            String postCloudinaryUrl  = cloudinaryHelper.uploadFile(createPostRequestDto.getUrl());
            // save post to database
            PostEntity postEntity = new PostEntity();
            postEntity.setUrl(postCloudinaryUrl);
            postEntity.setCaption(createPostRequestDto.getCaption());
            postEntity.setType(createPostRequestDto.getType());
            postRepo.save(postEntity);

            // find the user in the DB and add post into it
            UserEntity userEntity = userRepo.findByEmail(emailId).orElseThrow(() ->
                    new UserDoesNotExistException("User with the email doesn't exit", HttpStatus.NOT_FOUND));
            List<PostEntity> userExistingPostEntity = userEntity.getPostEntity();
            userExistingPostEntity.add(postEntity);
            userEntity.setPostEntity(userExistingPostEntity);
            userRepo.save(userEntity);

            log.info("Post successfully created for user: {}", emailId);
            return CreatePostDto.builder()
                    .type(createPostRequestDto.getType())
                    .url(postCloudinaryUrl)
                    .build();

        } catch (IOException ioException){
            log.error("Error uploading file");
            throw new ApiException(ioException);
        } catch (Exception exception){
            log.error("Failed to create post for user: {}", emailId);
            throw exception;
        }
    }
}
