package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.exception.TokenException;
import com.socialsphere.socialsphere.repository.RefreshTokenRepo;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    @Value("${refreshTokenExpiry}")
    private long refreshTokenExpiresIn;

    @Override
    public RefreshTokenEntity createRefreshToken(String username) {
        log.info("Creating new refresh token");
        try{
            UserEntity userEntity = userRepo.findByUsername(username);
            // Delete the refresh token if it's already exits for the current user
            refreshTokenRepo.findTokenByUserId(userEntity.getId())
                    .ifPresent(refreshTokenRepo::delete);
            RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                    .userEntity(userEntity)
                    .token(UUID.randomUUID().toString())
                    .expiresDate(Instant.now().plusMillis(refreshTokenExpiresIn))
                    .build();
            log.info("Created new refresh token");
            return refreshTokenRepo.save(refreshTokenEntity);
        } catch(Exception e){
            log.error("Error while creating refresh token", e);
            throw e;
        }
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if(token.getExpiresDate().compareTo(Instant.now()) < 0){
            refreshTokenRepo.delete(token);
            throw new TokenException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
