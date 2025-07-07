package com.socialsphere.socialsphere.services.impl;

import com.mongodb.MongoException;
import com.socialsphere.socialsphere.constant.CommonConstant;
import com.socialsphere.socialsphere.entity.OtpEntity;
import com.socialsphere.socialsphere.exception.OtpException;
import com.socialsphere.socialsphere.helper.EmailServiceHelper;
import com.socialsphere.socialsphere.repository.OtpRepo;
import com.socialsphere.socialsphere.services.SendOtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
@Service
@RequiredArgsConstructor
@Slf4j
public class SendOtpServiceImpl implements SendOtpService {

    private final OtpRepo otpRepo;

    private final EmailServiceHelper emailService;

    @Transactional
    @Override
    public String sendOtp(String email) {
        log.info("Entering into SendOtpServiceImpl service, sendOtp method");
        try{
            String otp = generateOtp();
            saveOtpToDatabase(email,otp);
            Context context = new Context();
            context.setVariable(CommonConstant.OTP,otp);
            emailService.sendEmail(email, CommonConstant.SIGN_UP_OTP_SUBJECT, context, "otpMailTemplate");
            log.info("Exiting from SendOtpServiceImpl service, sendOtp service");
            return otp;
        } catch(MailException | MessagingException | MongoException exception){
            log.error("Error occurred while sending otp", exception);
            throw new OtpException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            log.error("Error occurred while sending otp");
            throw e ;
        }
    }

    private String generateOtp() {
        log.info("Entering into SendOtpServiceImpl service, generateOtp method");
        // Creates a cryptographically strong random number generator
        // SecureRandom is better than Random for security purposes
        final SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        log.info("exiting from SendOtpServiceImpl service, generateOtp method, Generated otp: {}", otp);
        return String.valueOf(otp);
    }

    private void saveOtpToDatabase(String email, String otp) {
        log.info("Into SendOtpServiceImpl service saveOtpToDatabase method, Initiating Saving OTP in database call");
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setEmail(email);
        otpEntity.setCode(otp);
        otpRepo.save(otpEntity);
        log.info("Exiting from SendOtpServiceImpl service saveOtpDatabase method, OTP is successfully saved into database");
    }
}
