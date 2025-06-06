package com.socialsphere.socialsphere.services.impl;

import com.mongodb.MongoException;
import com.socialsphere.socialsphere.constant.CommonConstant;
import com.socialsphere.socialsphere.entity.OtpEntity;
import com.socialsphere.socialsphere.exception.OtpException;
import com.socialsphere.socialsphere.helper.EmailService;
import com.socialsphere.socialsphere.repository.OtpRepo;
import com.socialsphere.socialsphere.services.SendOtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
@Service
@RequiredArgsConstructor
@Slf4j
public class SendOtpServiceImpl implements SendOtpService {

    private final OtpRepo otpRepo;

    private final EmailService emailService;

    @Override
    public void sendOtp(String email) {
        log.info("Entering into sendOtp service");
        try{
            String otp = generateOtp();
            saveOtpToDatabase(email,otp);
            Context context = new Context();
            context.setVariable(CommonConstant.OTP,otp);
            emailService.sendEmail(email, CommonConstant.SIGN_UP_OTP_SUBJECT, context);
        } catch(MailException | MessagingException | MongoException exception){
            log.error("Error occurred while sending otp", exception);
            throw new OtpException("Otp Send failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Exiting from sendOtp service");
    }

    private String generateOtp() {
        final SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    private void saveOtpToDatabase(String email, String otp) {
        log.info("Saving OTP in database");
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(email);
        otpEntity.setCode(otp);
        otpRepo.save(otpEntity);
        log.info("Saved OTP in database");
    }
}
