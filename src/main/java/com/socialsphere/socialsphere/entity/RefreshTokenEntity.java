package com.socialsphere.socialsphere.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "refreshToken")
@Data
public class RefreshTokenEntity {
    @Id
    private ObjectId id;
    private String token;
    private Instant expiresDate;

    @DocumentReference(lazy = true)
    private UserEntity userEntity;
}
