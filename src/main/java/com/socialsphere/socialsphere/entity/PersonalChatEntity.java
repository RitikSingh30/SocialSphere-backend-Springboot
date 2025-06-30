package com.socialsphere.socialsphere.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "personal_chat")
@Data
public class PersonalChatEntity {
    @Id
    private ObjectId id;
    @NotBlank(message = "Sender full name is required")
    private String senderFullName;
    @NotBlank(message = "Sender user name is required")
    private String senderUserName;
    @NotBlank(message = "Sender profile picture is required")
    private String senderProfilePicture;
    @NotBlank(message = "Receiver user name is required")
    private String receiverUserName;
    @NotBlank(message = "Content is required")
    private String content;
    @NotBlank(message = "CreatedAt date is required")
    private String createdAt;
}
