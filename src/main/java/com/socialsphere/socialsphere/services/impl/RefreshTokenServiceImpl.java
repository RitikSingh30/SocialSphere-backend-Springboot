package com.socialsphere.socialsphere.services.impl;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import com.socialsphere.socialsphere.exception.TokenException;
import com.socialsphere.socialsphere.repository.RefreshTokenRepo;
import com.socialsphere.socialsphere.repository.UserRepo;
import com.socialsphere.socialsphere.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    @Value("${refreshTokenExpiry}")
    private long refreshTokenExpiresIn;

    @Override
    public RefreshTokenEntity createRefreshToken(String username) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userEntity(userRepo.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiresDate(Instant.now().plusMillis(refreshTokenExpiresIn))
                .build();
        return refreshTokenRepo.save(refreshTokenEntity);
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
