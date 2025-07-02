package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.exception.UserAlreadyExistException;
import com.socialsphere.socialsphere.payload.request.SignupVerificationRequestDto;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.SendOtpService;
import com.socialsphere.socialsphere.services.SignupVerificationService;
import com.socialsphere.socialsphere.utility.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupVerificationServiceImpl implements SignupVerificationService {
    private final UserRepo userRepo;
    private final SendOtpService sendOtpService;

    @Override
    public Map<String,Object> signupVerification(SignupVerificationRequestDto signupVerificationRequestDto) {
        String otp = null;
        try{
            log.info("Entering into SignupVerificationServiceImpl, signupVerification method");
            log.info("Checking if user exists with username {} or email {}", signupVerificationRequestDto.getUserName(), signupVerificationRequestDto.getEmail());
            if(userRepo.findByUsername(signupVerificationRequestDto.getUserName().toLowerCase()) != null
                    || userRepo.findByEmail(signupVerificationRequestDto.getEmail().toLowerCase()).isPresent()) {
                throw new UserAlreadyExistException("User with the username or email already exist please proceed to login", HttpStatus.CONFLICT);
            }
            // Sending otp to user after verification
            otp = sendOtpService.sendOtp(signupVerificationRequestDto.getEmail());
        } catch (Exception e){
            log.error("Exception occurred while signing up verification");
            throw e;
        }
        log.info("Exiting from SignupVerificationServiceImpl, signupVerification method");
        return CommonUtil.prepareOtpDataResponse(otp, signupVerificationRequestDto.getEmail());
    }


}
