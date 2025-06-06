package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<UserEntity, ObjectId> {
    public UserEntity findByUsername(@NotEmpty String username);
}
