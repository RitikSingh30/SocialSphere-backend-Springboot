package com.socialsphere.socialsphere.helper;

import com.socialsphere.socialsphere.entity.OtpEntity;
import com.socialsphere.socialsphere.exception.OtpException;
import com.socialsphere.socialsphere.repository.OtpRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateOtpHelper {
    private final OtpRepo otpRepo;

    public Map<String,Object> isOtpValid(String otp, String emailId) {
        try{
            log.info("Validating OTP for user {}", emailId);
            log.info("Calling otpRepo to fetch the latest otp");
            // fetch the latest otp of the user from the database
            OtpEntity otpEntity = otpRepo.findTopByEmailOrderByCreatedAtDesc(emailId)
                    .orElseThrow(() -> new OtpException("Otp verification fail, please try again", HttpStatus.INTERNAL_SERVER_ERROR));

            // comparing the otp
            if(!otpEntity.getCode().equals(otp)){
                throw new OtpException("Invalid OTP, Please enter a valid OTP", HttpStatus.BAD_REQUEST);
            }
            log.info("Otp verified successfully");
        } catch(OtpException otpException){
            log.error("Otp exception occurred in isOtpValid method", otpException);
            throw otpException;
        } catch (Exception e){
            log.error("Error occurred while validating OTP for user {}", emailId, e);
        }
        return Map.of(
                "otp",otp,
                "email",emailId);
    }
}
