package com.socialsphere.socialsphere.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "post")
@Data
public class Post {
    @Id
    private ObjectId id;
    @NotBlank(message = "Url should be present")
    private String url;
    @NotBlank(message = "Caption should be present")
    private String caption;
    @NotBlank(message = "Type of post should be present")
    private String type;
    @DocumentReference(lazy = true)
    @NotBlank(message = "User Entity should be present")
    private List<UserEntity> like = new ArrayList<>();
    @NotBlank(message = "Comment should be present")
    @DocumentReference(lazy = true)
    private List<Comment> comment = new ArrayList<>();
}
