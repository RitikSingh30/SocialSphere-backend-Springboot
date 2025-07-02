package com.socialsphere.socialsphere.entity;

import com.socialsphere.socialsphere.utility.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

// TODO when sending the userData to the frontEnd make sure that userEntityDto should match the field name same as the old MongoDB field name otherwise the frontEnd will throw the error (Captial first letter)
@Document(collection = "userProfile")
@Data
public class UserEntity {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NotBlank(message = "UserName should be present")
    private String username;
    @NotBlank(message = "Name should be present")
    private String fullName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should be present")
    private String email;
    @NotBlank(message = "Password should be present")
    private String password;
    private String bio;
    @DocumentReference(lazy = true)
    private List<PostEntity> postEntity = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<PostEntity> savedPostEntity = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<UserEntity> followers = new ArrayList<>();
    @DocumentReference(lazy = true)
    private List<UserEntity> following = new ArrayList<>();
    @NotBlank(message = "Profile picture should be present")
    private String profilePicture;
    private Gender gender;
    @DocumentReference(lazy = true)
    private List<PersonalChatHistoryEntity> personalChatHistoryEntities = new ArrayList<>();

}
