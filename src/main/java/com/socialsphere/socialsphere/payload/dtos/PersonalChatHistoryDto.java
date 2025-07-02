package com.socialsphere.socialsphere.payload.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonalChatHistoryDto {
    private List<PersonalChatDto> personalChats;
}
