package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.exception.UserDoesNotExistException;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.ForgotPasswordService;
import com.socialsphere.socialsphere.services.SendOtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserRepo userRepo;
    private final SendOtpService sendOtpService;

    @Override
    public void forgotPassword(String emailId) {
        try{
            log.info("Entering into ForgotPasswordServiceImpl service, forgotPassword method");
            userRepo.findByEmail(emailId).orElseThrow(() -> new UserDoesNotExistException("User Doesn't exist", HttpStatus.NOT_FOUND));
            sendOtpService.sendOtp(emailId);
            log.info("Exiting from ForgotPasswordServiceImpl service, forgotPassword method");
        } catch (Exception e) {
            log.error("Error while trying to forgotPassword", e);
            throw e;
        }
    }
}
