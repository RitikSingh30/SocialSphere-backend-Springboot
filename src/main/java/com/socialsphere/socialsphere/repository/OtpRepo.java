package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.OtpEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepo extends MongoRepository<OtpEntity, ObjectId> {
    Optional<OtpEntity> findTopByEmailOrderByCreatedAtDesc(String email);
}
