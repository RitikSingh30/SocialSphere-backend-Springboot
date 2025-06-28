package com.socialsphere.socialsphere.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document(collection = "reset_token")
public class ResetTokenEntity {
    @Id
    private ObjectId id;
    @NotBlank(message = "Token should be present")
    private String token;
    @NotBlank(message = "ExpireAt should be present")
    private LocalDateTime expiresAt;
    @NotBlank(message = "UserEntity should be present")
    @DocumentReference(lazy = true)
    private UserEntity userEntity;

    public boolean isExpired(){
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
