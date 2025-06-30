package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.payload.dtos.UserDto;

public interface UserOperationService {
    UserDto getUserData(String emailId, boolean allData);
}
