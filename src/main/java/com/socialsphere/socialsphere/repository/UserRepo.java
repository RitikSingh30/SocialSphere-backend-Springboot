package com.socialsphere.socialsphere.repository;

import com.socialsphere.socialsphere.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserEntity, ObjectId> {
    Optional<UserEntity> findByUsername(@NotEmpty String username);
    // find the user using regex and email should not be equal to input email and only return the username and profilePicture
    Optional<UserEntity> findByEmail(@NotEmpty String email);
    @Query(value = "{ 'username': { $regex: ?0, $options: 'i' }, 'email': { $ne: ?1 } }",
            fields = "{ 'username': 1, 'profilePicture': 1 }")
    List<UserEntity> findByUserNameRegexIgnoreCaseAndEmailNot(String searchUserName, String email);
}
