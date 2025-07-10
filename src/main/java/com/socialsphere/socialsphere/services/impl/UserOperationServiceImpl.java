package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.ApiException;
import com.socialsphere.socialsphere.exception.UserDoesNotExistException;
import com.socialsphere.socialsphere.helper.CloudinaryHelper;
import com.socialsphere.socialsphere.mapper.UserEntityMapper;
import com.socialsphere.socialsphere.payload.dtos.BasicUserInfoDto;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class UserOperationServiceImpl implements UserOperationService {

    private final UserRepo userRepo;
    private final UserEntityMapper userEntityMapper;
    private final CloudinaryHelper cloudinaryHelper;

    @Override
    public UserDto getUserData(String username, boolean allData) {
        log.info("Entering into UserOperationServiceImpl, getUserData method");
        try{
            UserEntity userEntity = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UserDoesNotExistException("User with the username does not exist", HttpStatus.NOT_FOUND));
            UserDto userDto = userEntityMapper.getUserResponseDto(userEntity);
            // checking if the data is required for the current user or different user
            if(!allData) {
                userDto.setPost(null);
                userDto.setFollowers(null);
                userDto.setFollowing(null);
            }
            log.info("Exiting from UserOperationServiceImpl, getUserData method");
            return userDto;
        } catch(Exception exception){
            log.error("Exception occurred while getting the user data in UserOperationServiceImpl, getUserData method");
            throw exception;
        }
    }

    @Transactional
    @Override
    public UserUpdateDto updateUserProfileInfo(UserUpdateDto userUpdateDto, String username) {
        log.info("Entering into UserOperationServiceImpl.updateUserProfileInfo");
        try{
            UserUpdateDto userUpdateDtoResponse = new UserUpdateDto();
            UserEntity userEntity = userRepo.findByUsername(username)
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

            log.info("Exiting from UserOperationServiceImpl.updateUserProfileInfo");
            return userUpdateDtoResponse;

        } catch (IOException ioException){
            log.error("Error occurred while uploading file to cloudinary");
            throw new ApiException(ioException);
        } catch (Exception exception){
            log.error("Exception occurred while updating the user profile info");
            throw exception;
        }
    }

    @Override
    public List<BasicUserInfoDto> searchUser(String emailId, String userName) {
        log.info("Entering into UserOperationServiceImpl.searchUser");
        try{
            List<UserEntity> searchUserResult = userRepo.findByUserNameRegexIgnoreCaseAndEmailNot(userName,emailId);
            List<BasicUserInfoDto> basicUserInfoDtoList = searchUserResult.stream()
                    .filter(Objects::nonNull)
                    .map(userEntity ->
                    BasicUserInfoDto.builder()
                            .username(userEntity.getUsername())
                            .profilePicture(userEntity.getProfilePicture())
                            .build())
                    .toList();
            log.info("Exiting from UserOperationServiceImpl.searchUser");
            return basicUserInfoDtoList;
        } catch (Exception exception){
            log.error("Exception occurred while searching the user profile info");
            throw exception;
        }
    }
}
