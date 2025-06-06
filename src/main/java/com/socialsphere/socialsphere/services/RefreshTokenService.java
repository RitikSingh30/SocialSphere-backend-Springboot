package com.socialsphere.socialsphere.services;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(String username);
    Optional<RefreshTokenEntity> findByToken(String token);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
}
