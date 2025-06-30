package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonalChatDto {
    private String senderFullName;
    private String senderUserName;
    private String senderProfilePicture;
    private String receiverUserName;
    private String content;
    private String createdAt;
}
