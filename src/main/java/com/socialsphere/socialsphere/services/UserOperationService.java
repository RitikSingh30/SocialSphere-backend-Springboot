package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.payload.dtos.UserUpdateDto;

public interface UserOperationService {
    UserDto getUserData(String emailId, boolean allData);
    UserUpdateDto updateUserProfileInfo(UserUpdateDto userUpdateDto, String emailId);
}
