package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.RefreshTokenEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends MongoRepository<RefreshTokenEntity, ObjectId> {
    Optional<RefreshTokenEntity> findByToken(String token);
}
