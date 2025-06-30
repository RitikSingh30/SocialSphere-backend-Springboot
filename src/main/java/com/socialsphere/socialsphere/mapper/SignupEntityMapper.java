package com.socialsphere.socialsphere.mapper;

import com.socialsphere.socialsphere.constant.CommonConstant;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.payload.request.SignupRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignupEntityMapper {

    public UserEntity setUserProfileEntity(SignupRequestDto signupRequestDto, String encodedPassword) {
        log.info("Mapping the signupDto data to userEntity journey started");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(signupRequestDto.getEmail().toLowerCase());
        userEntity.setUsername(signupRequestDto.getUserName().toLowerCase());
        userEntity.setPassword(encodedPassword);
        userEntity.setFullName(signupRequestDto.getFullName());
        userEntity.setProfilePicture(CommonConstant.USER_DEFAULT_PROFILE_PICTURE);
        log.info("Mapping the signupDto data to userEntity journey completed");
        return userEntity;
    }
}
