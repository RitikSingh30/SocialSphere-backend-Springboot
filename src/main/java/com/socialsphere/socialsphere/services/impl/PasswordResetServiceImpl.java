package com.socialsphere.socialsphere.services.impl;

import com.mongodb.MongoException;
import com.socialsphere.socialsphere.entity.ResetTokenEntity;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.OtpException;
import com.socialsphere.socialsphere.helper.EmailServiceHelper;
import com.socialsphere.socialsphere.payload.PasswordResetConfirmRequestDto;
import com.socialsphere.socialsphere.repository.ResetTokenRepo;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.PasswordResetService;
import com.socialsphere.socialsphere.utility.CommonUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepo userRepo;
    private final ResetTokenRepo resetTokenRepo;
    private final EmailServiceHelper emailServiceHelper;
    private final PasswordEncoder passwordEncoder;

    @Value("${password.reset.base.url}")
    private String passwordResetBaseUrl;

    @Override
    public void resetPassword(String email) {
        log.info("Entering into PasswordResetServiceImpl service, resetPassword method");
        try{
            Optional<UserEntity> userEntityOptional = userRepo.findByEmail(email);
            if(userEntityOptional.isEmpty()) return;

            UserEntity userEntity = userEntityOptional.get();
            // create reset token
            String token = CommonUtil.makeResetToken();

            // save reset token into the DB
            ResetTokenEntity resetTokenEntity = new ResetTokenEntity();
            resetTokenEntity.setToken(token);
            resetTokenEntity.setUserEntity(userEntity);
            resetTokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            resetTokenRepo.save(resetTokenEntity);

            // create reset password URL
            String resetUrl = passwordResetBaseUrl + token;

            Context context = new Context();
            context.setVariable("token", token);
            context.setVariable("resetUrl", resetUrl);

            // send reset password email
            emailServiceHelper.sendEmail(email,"Reset Password", context, "resetPasswordMailTemplate");

        } catch(MailException | MessagingException | MongoException exception){
            log.error("Error occurred while sending otp", exception);
            throw new OtpException("Otp Send failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            log.error("Error occurred while sending otp", e);
            throw e ;
        }
        log.info("Exiting from PasswordResetServiceImpl service, resetPassword method");
    }

    @Override
    public void confirmResetPassword(ResetTokenEntity tokenRecord, PasswordResetConfirmRequestDto passwordResetConfirmRequestDto){
        log.info("Entering into PasswordResetServiceImpl service, confirmResetPassword method");
        try{
            // Get the userEntity associated with the current token & unwrap the proxy to get the real user data
            UserEntity userEntity = (UserEntity) ((LazyLoadingProxy) tokenRecord.getUserEntity()).getTarget();
            // change user password and save into DB
            userEntity.setPassword(passwordEncoder.encode(passwordResetConfirmRequestDto.getNewPassword()));
            userRepo.save(userEntity);
            // delete the token after changing user password
            resetTokenRepo.delete(tokenRecord);
        } catch(MongoException exception){
            log.error("Error occurred while resetting the user password and saving the user", exception);
            throw new MongoException("Error occurred while resetting the user password and saving the user", exception);
        } catch(Exception e){
            log.error("Error occurred in confirm resetPassword method", e);
            throw e ;
        }
        log.info("Exiting from PasswordResetServiceImpl service, confirmResetPassword method");
    }

}
