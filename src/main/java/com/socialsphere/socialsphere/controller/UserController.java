package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.payload.dtos.UserUpdateDto;
import com.socialsphere.socialsphere.payload.response.ApiResponse;
import com.socialsphere.socialsphere.services.UserOperationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.socialsphere.socialsphere.utility.CommonUtil.getSuccessApiResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserOperationService userOperationService;

    @GetMapping("/get-user-data/{emailId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserData(@PathVariable String emailId, @RequestParam boolean allData,
                                                     HttpServletRequest request){
        log.info("Get user date journey started from Controller");
        UserDto userDto = userOperationService.getUserData(emailId, allData);
        log.info("Get user date journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("User data fetched successful..!",userDto,request));
    }

    @PutMapping("/update-user-profile-info/{emailId}")
    public ResponseEntity<ApiResponse<UserUpdateDto>> updateUserProfileInfo(@RequestBody UserUpdateDto userUpdateDto,
                                                                                 @PathVariable String emailId, HttpServletRequest request){
        log.info("Update user profile info journey started from Controller");
        UserUpdateDto userUpdateDtoResponse = userOperationService.updateUserProfileInfo(userUpdateDto,emailId);
        log.info("Update user profile info journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("User profile info updated successful..!",userUpdateDtoResponse,request));
    }
}
