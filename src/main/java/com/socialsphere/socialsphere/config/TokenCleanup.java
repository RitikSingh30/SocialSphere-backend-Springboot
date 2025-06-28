package com.socialsphere.socialsphere.config;

import com.socialsphere.socialsphere.repository.ResetTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TokenCleanup {
    private final ResetTokenRepo resetTokenRepo;

    @Scheduled(fixedRate = 3_600_000) // run every hour
    public void clearExpiredTokens() {
        resetTokenRepo.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}
