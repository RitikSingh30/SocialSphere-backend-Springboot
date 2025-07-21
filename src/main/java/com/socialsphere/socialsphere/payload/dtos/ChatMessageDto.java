package com.socialsphere.socialsphere.payload.dtos;

import com.socialsphere.socialsphere.utility.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageDto {
    private MessageType type;
    private String content;
    private String sender;
}
