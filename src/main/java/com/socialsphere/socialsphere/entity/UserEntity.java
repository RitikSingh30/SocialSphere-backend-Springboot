package com.socialsphere.socialsphere.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
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
}
