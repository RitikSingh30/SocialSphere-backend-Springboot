package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.UserDoesNotExistException;
import com.socialsphere.socialsphere.mapper.UserEntityMapper;
import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.UserOperationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class UserOperationServiceImpl implements UserOperationService {

    private final UserRepo userRepo;
    private final UserEntityMapper userEntityMapper;

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
        } catch(Exception e){
            log.error("Exception occurred while getting the user data in UserOperationServiceImpl, getUserData method", e);
            throw e;
        }
        log.info("Exiting into UserOperationServiceImpl, getUserData method");
        return userDto;
    }
}
