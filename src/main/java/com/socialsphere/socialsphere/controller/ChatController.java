package com.socialsphere.socialsphere.controller;

import com.socialsphere.socialsphere.payload.dtos.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessageDto) {
        return chatMessageDto;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        // Add username in web socket session
        simpMessageHeaderAccessor.getSessionAttributes().put("username", chatMessageDto.getSender());
        return chatMessageDto;
    }
}
