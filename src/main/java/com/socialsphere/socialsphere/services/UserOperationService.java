package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.dtos.BasicUserInfoDto;
import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.payload.dtos.UserUpdateDto;

import java.util.List;

public interface UserOperationService {
    UserDto getUserData(String username, boolean allData);
    UserUpdateDto updateUserProfileInfo(UserUpdateDto userUpdateDto, String username);
    List<BasicUserInfoDto> searchUser(String emailId, String userName);
}
