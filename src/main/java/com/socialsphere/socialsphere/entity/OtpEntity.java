package com.socialsphere.socialsphere.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "otp")
public class OtpEntity {
    @Id
    private ObjectId id;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should be present")
    private String email;
    @CreatedDate
    private Date createdAt;
    @NotBlank(message = "otp code should be present")
    private String code;
}
