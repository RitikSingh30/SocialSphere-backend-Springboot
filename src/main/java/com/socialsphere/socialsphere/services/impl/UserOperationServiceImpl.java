package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.ApiException;
import com.socialsphere.socialsphere.exception.UserDoesNotExistException;
import com.socialsphere.socialsphere.helper.CloudinaryHelper;
import com.socialsphere.socialsphere.mapper.UserEntityMapper;
import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.payload.dtos.UserUpdateDto;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.UserOperationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class UserOperationServiceImpl implements UserOperationService {

    private final UserRepo userRepo;
    private final UserEntityMapper userEntityMapper;
    private final CloudinaryHelper cloudinaryHelper;

    @Override
    public UserDto getUserData(String emailId, boolean allData) {
        log.info("Entering into UserOperationServiceImpl, getUserData method");
        UserDto userDto = null;
        try{
            UserEntity userEntity = userRepo.findByEmail(emailId.toLowerCase())
                    .orElseThrow(() -> new UserDoesNotExistException("User with the username does not exist", HttpStatus.NOT_FOUND));
            userDto = userEntityMapper.getUserResponseDto(userEntity);
            // checking if the data is required for the current user or different user
            if(!allData) {
                userDto.setPost(null);
                userDto.setFollowers(null);
                userDto.setFollowing(null);
            }
        } catch(Exception exception){
            log.error("Exception occurred while getting the user data in UserOperationServiceImpl, getUserData method");
            throw exception;
        }
        log.info("Exiting from UserOperationServiceImpl, getUserData method");
        return userDto;
    }

    @Transactional
    @Override
    public UserUpdateDto updateUserProfileInfo(UserUpdateDto userUpdateDto, String emailId) {
        log.info("Entering into UserOperationServiceImpl, updateUserProfileInfo method");
        UserUpdateDto userUpdateDtoResponse = new UserUpdateDto();
        try{
            UserEntity userEntity = userRepo.findByEmail(emailId.toLowerCase())
                    .orElseThrow(() -> new UserDoesNotExistException("User with the username does not exist", HttpStatus.NOT_FOUND));

            if(userUpdateDto.getUsername() != null){
                userEntity.setUsername(userUpdateDto.getUsername());
                userUpdateDtoResponse.setUsername(userEntity.getUsername());
            }
            if(userUpdateDto.getBio() != null){
                userEntity.setBio(userUpdateDto.getBio());
                userUpdateDtoResponse.setBio(userEntity.getBio());
            }
            if(userUpdateDto.getGender() != null){
                userEntity.setGender(userUpdateDto.getGender());
                userUpdateDtoResponse.setGender(userEntity.getGender());
            }
            if(userUpdateDto.getProfilePicture() != null){
                String profilePictureUploadUrl = cloudinaryHelper.uploadFile(userUpdateDto.getProfilePicture());
                userEntity.setProfilePicture(profilePictureUploadUrl);
                userUpdateDtoResponse.setProfilePicture(userEntity.getProfilePicture());
            }

            userRepo.save(userEntity);
        } catch (IOException ioException){
            log.error("Error occurred while uploading file to cloudinary");
            throw new ApiException(ioException);
        } catch (Exception exception){
            log.error("Exception occurred while updating the user profile info");
            throw exception;
        }
        log.info("Exiting from UserOperationServiceImpl, updateUserProfileInfo method");
        return userUpdateDtoResponse;
    }
}
