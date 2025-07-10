package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.payload.dtos.BasicUserInfoDto;
import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.payload.dtos.UserUpdateDto;
import com.socialsphere.socialsphere.payload.response.ApiResponse;
import com.socialsphere.socialsphere.services.UserOperationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.socialsphere.socialsphere.utility.CommonUtil.getSuccessApiResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserOperationService userOperationService;

    @PreAuthorize("#username == authentication.name")
    @GetMapping("/get-user-data/{username}")
    public ResponseEntity<ApiResponse<UserDto>> getUserData(@PathVariable String username, @RequestParam boolean allData,
                                                     HttpServletRequest request){
        log.info("Get user date journey started from Controller");
        UserDto userDto = userOperationService.getUserData(username, allData);
        log.info("Get user date journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("User data fetched successful..!",userDto,request));
    }

    @PreAuthorize("#username == authentication.name")
    @PutMapping("/update-user-profile-info/{username}")
    public ResponseEntity<ApiResponse<UserUpdateDto>> updateUserProfileInfo(@RequestBody UserUpdateDto userUpdateDto,
                                                                                 @PathVariable String username, HttpServletRequest request){
        log.info("Update user profile info journey started from Controller");
        UserUpdateDto userUpdateDtoResponse = userOperationService.updateUserProfileInfo(userUpdateDto,username);
        log.info("Update user profile info journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("User profile info updated successful..!",userUpdateDtoResponse,request));
    }

    @GetMapping("/search-user/{emailId}")
    public ResponseEntity<ApiResponse<List<BasicUserInfoDto>>> searchUser(@PathVariable String emailId, @RequestParam String userName, HttpServletRequest request){
        log.info("Search user journey started from Controller");
        List<BasicUserInfoDto> basicUserInfoDto = userOperationService.searchUser(emailId,userName);
        log.info("Search user journey completed from Controller");
        return ResponseEntity.ok(getSuccessApiResponse("User search success",basicUserInfoDto,request));
    }
}
