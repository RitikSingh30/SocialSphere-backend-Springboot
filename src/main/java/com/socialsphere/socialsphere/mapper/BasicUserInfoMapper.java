package com.socialsphere.socialsphere.mapper;

import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.payload.dtos.BasicUserInfoDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicUserInfoMapper {
    private BasicUserInfoMapper() {}
    public static BasicUserInfoDto getBasicUserDto(UserEntity userEntity){
        log.info("Mapping UserEntity to BasicUserInfoDto in BasicUserInfoMapper.getBasicUserDto");
        return BasicUserInfoDto.builder()
                .username(userEntity.getUsername())
                .profilePicture(userEntity.getProfilePicture())
                .build();
    }
}
