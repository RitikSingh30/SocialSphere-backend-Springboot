package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.payload.dtos.UserDto;
import com.socialsphere.socialsphere.payload.response.ApiResponse;
import com.socialsphere.socialsphere.services.UserOperationService;
import com.socialsphere.socialsphere.utility.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserOperationService userOperationService;

    @GetMapping("/getUserData/{emailId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserData(@PathVariable String emailId, @RequestParam boolean allData,
                                                     HttpServletRequest request){
        log.info("Get user date journey started from Controller");
        UserDto userDto = userOperationService.getUserData(emailId, allData);
        log.info("Get user date journey completed from Controller");
        return ResponseEntity.ok(CommonUtil.getSuccessApiResponse("User data fetched successful..!",userDto,request));
    }
}
