package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByUsername(@NotEmpty String username);
    Optional<UserEntity> findByEmail(@NotEmpty String email);
}
