package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.exception.UserAlreadyExistException;
import com.socialsphere.socialsphere.payload.SignupVerificationDto;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.SendOtpService;
import com.socialsphere.socialsphere.services.SignupVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupVerificationServiceImpl implements SignupVerificationService {
    private final UserRepo userRepo;
    private final SendOtpService sendOtpService;

    @Override
    public void signupVerification(SignupVerificationDto signupVerificationDto) {
        try{
            log.info("Entering into SignupVerificationService, signupVerification method");
            log.info("Checking if user exists with username {} or email {}", signupVerificationDto.getUserName(), signupVerificationDto.getEmail());
            if(userRepo.findByUsername(signupVerificationDto.getUserName().toLowerCase()) != null
                    || userRepo.findByEmail(signupVerificationDto.getEmail().toLowerCase()).isPresent()) {
                throw new UserAlreadyExistException("User with the username or email already exist please proceed to login", HttpStatus.CONFLICT);
            }
            // Sending otp to user after verification
            sendOtpService.sendOtp(signupVerificationDto.getEmail());
        } catch (Exception e){
            log.error("Exception occurred while signing up verification", e);
            throw e;
        }
    }
}
