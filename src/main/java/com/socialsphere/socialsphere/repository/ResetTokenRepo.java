package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.ResetTokenEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetTokenRepo extends MongoRepository<ResetTokenEntity, ObjectId> {
    Optional<ResetTokenEntity> findByToken(String token);
    void deleteAllByExpiresAtBefore(LocalDateTime expiresAt);
}
