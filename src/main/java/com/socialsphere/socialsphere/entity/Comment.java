package com.socialsphere.socialsphere.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "comment")
@Data
public class Comment {
    @Id
    private ObjectId id;
    @NotBlank(message = "User entity should be present")
    @DocumentReference(lazy = true)
    private UserEntity userEntity;
    @NotBlank(message = "Content should be present")
    private String content;
}
