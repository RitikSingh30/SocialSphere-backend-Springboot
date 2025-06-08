package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.OtpEntity;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.OtpException;
import com.socialsphere.socialsphere.exception.UserAlreadyExistException;
import com.socialsphere.socialsphere.mapper.SignupEntityMapper;
import com.socialsphere.socialsphere.payload.SignupDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;
import com.socialsphere.socialsphere.repository.OtpRepo;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SignupServiceImpl implements SignupService {
    private final OtpRepo otpRepo;
    private final PasswordEncoder passwordEncoder;
    private final SignupEntityMapper signupEntityMapper;
    private final UserRepo userRepo;

    @Override
    public SignupResponseDto signup(SignupDto signupDto) {
        try{
            log.info("Entering into SignupService, signup method");
            log.info("Checking if user exists with username {}", signupDto.getUserName());
            if(userRepo.findByUsername(signupDto.getUserName().toLowerCase()) != null || userRepo.findByEmail(signupDto.getEmail().toLowerCase()) != null) {
                throw new UserAlreadyExistException("User with the username or email already exist please proceed to login", HttpStatus.CONFLICT);
            }
            log.info("Calling otpRepo to fetch the latest otp");
            // fetch the latest otp of the user from the database
            OtpEntity otpEntity = otpRepo.findTopByEmailOrderByCreatedAtDesc(signupDto.getEmail())
                    .orElseThrow(() -> new OtpException("Otp verification fail, please try again", HttpStatus.INTERNAL_SERVER_ERROR));

            // comparing the otp
            if(!otpEntity.getCode().equals(signupDto.getOtp())){
                throw new OtpException("Invalid OTP, Please enter a valid OTP", HttpStatus.BAD_REQUEST);
            }

            // encrypt the password and save into database
            String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
            UserEntity userProfileEntity = signupEntityMapper.setUserProfileEntity(signupDto, encodedPassword);
            log.info("Calling userRepo to save the user signup data into database");
            userRepo.save(userProfileEntity);

        } catch (UserAlreadyExistException userAlreadyExistException) {
            log.error("User {} already exist", signupDto.getUserName());
            throw userAlreadyExistException;
        } catch (OtpException otpException) {
            log.error("Otp verification failed", otpException);
            throw otpException;
        } catch (Exception e) {
            log.error("Error occur while signing up", e);
            throw e;
        }
        log.info("Exiting from Signup service, signup method");
        return SignupResponseDto.builder()
                .message("Signup successful")
                .success(true)
                .build();
    }

}
