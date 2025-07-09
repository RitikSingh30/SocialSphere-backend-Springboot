package com.socialsphere.socialsphere.mapper;

import com.socialsphere.socialsphere.entity.*;
import com.socialsphere.socialsphere.payload.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class UserEntityMapper {
    public UserDto getUserResponseDto(UserEntity userEntity) {
        log.info("Converting UserEntity to UserDto in UserEntityMapper.getUserResponseDto");
        return UserDto.builder()
                .username(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .post(PostMapper.getListPostDto(userEntity.getPostEntity()))
                .savedPost(PostMapper.getListPostDto(userEntity.getSavedPostEntity()))
                .followers(getUserFollowersAndFollowing(userEntity.getFollowers()))
                .following(getUserFollowersAndFollowing(userEntity.getFollowing()))
                .profilePicture(userEntity.getProfilePicture())
                .gender(userEntity.getGender())
                .personalChatDto(getPersonalChatDto(userEntity.getPersonalChatHistoryEntities()))
                .build();
    }

    private List<PersonalChatHistoryDto> getPersonalChatDto(List<PersonalChatHistoryEntity> personalChatsEntity) {
        return Optional.ofNullable(personalChatsEntity)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.getPersonalChatEntities() != null && !entity.getPersonalChatEntities().isEmpty())
                .map(entity -> PersonalChatHistoryDto.builder()
                        .personalChats(
                                entity.getPersonalChatEntities().stream()
                                        .filter(Objects::nonNull)
                                        .map(this::personalChatEntityToPersonalChatDto)
                                        .toList()
                        )
                        .build()
                )
                .toList();
    }

    private PersonalChatDto personalChatEntityToPersonalChatDto(PersonalChatEntity personalChatEntity) {
        return PersonalChatDto.builder()
                .senderFullName(personalChatEntity.getSenderFullName())
                .senderUserName(personalChatEntity.getSenderUserName())
                .senderProfilePicture(personalChatEntity.getSenderProfilePicture())
                .receiverUserName(personalChatEntity.getReceiverUserName())
                .content(personalChatEntity.getContent())
                .createdAt(personalChatEntity.getCreatedAt())
                .build();
    }

    private List<BasicUserInfoDto> getUserFollowersAndFollowing(List<UserEntity> userFollowersOrFollowing) {
        return userFollowersOrFollowing.stream()
                .filter(Objects::nonNull)
                .map(BasicUserInfoMapper::getBasicUserDto)
                .toList();
    }
}
