package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.OtpException;
import com.socialsphere.socialsphere.exception.UserAlreadyExistException;
import com.socialsphere.socialsphere.helper.ValidateOtpHelper;
import com.socialsphere.socialsphere.mapper.SignupEntityMapper;
import com.socialsphere.socialsphere.payload.SignupRequestDto;
import com.socialsphere.socialsphere.payload.response.SignupResponseDto;
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
    private final PasswordEncoder passwordEncoder;
    private final SignupEntityMapper signupEntityMapper;
    private final UserRepo userRepo;
    private final ValidateOtpHelper validateOtpHelper;

    @Override
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        try{
            log.info("Entering into SignupService, signup method");
            log.info("Checking if user exists with username {} or email {}", signupRequestDto.getUserName(), signupRequestDto.getEmail());
            if(userRepo.findByUsername(signupRequestDto.getUserName().toLowerCase()) != null || userRepo.findByEmail(signupRequestDto.getEmail().toLowerCase()).isPresent()) {
                throw new UserAlreadyExistException("User with the username or email already exist please proceed to login", HttpStatus.CONFLICT);
            }
            validateOtpHelper.isOtpValid(signupRequestDto.getOtp(), signupRequestDto.getEmail());

            // encrypt the password and save into database
            String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
            UserEntity userProfileEntity = signupEntityMapper.setUserProfileEntity(signupRequestDto, encodedPassword);
            log.info("Calling userRepo to save the user signup data into database");
            userRepo.save(userProfileEntity);

        } catch (UserAlreadyExistException userAlreadyExistException) {
            log.error("User {} already exist", signupRequestDto.getUserName());
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
                .username(signupRequestDto.getUserName())
                .email(signupRequestDto.getEmail())
                .fullName(signupRequestDto.getFullName())
                .build();
    }

}
