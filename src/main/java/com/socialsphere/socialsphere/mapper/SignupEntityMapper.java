package com.socialsphere.socialsphere.mapper;

import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.payload.SignupDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignupEntityMapper {

    public UserEntity setUserProfileEntity(SignupDto signupDto, String encodedPassword) {
        log.info("Mapping the signupDto data to userEntity journey started");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(signupDto.getEmail().toLowerCase());
        userEntity.setUsername(signupDto.getUserName().toLowerCase());
        userEntity.setPassword(encodedPassword);
        userEntity.setFullName(signupDto.getFullName());
        log.info("Mapping the signupDto data to userEntity journey completed");
        return userEntity;
    }
}
