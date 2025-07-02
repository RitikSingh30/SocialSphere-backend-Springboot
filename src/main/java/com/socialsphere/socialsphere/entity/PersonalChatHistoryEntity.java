package com.socialsphere.socialsphere.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "personal_chat_history")
@Data
public class PersonalChatHistoryEntity {
    @DocumentReference(lazy = true)
    private List<PersonalChatEntity> personalChatEntities = new ArrayList<>();
}
