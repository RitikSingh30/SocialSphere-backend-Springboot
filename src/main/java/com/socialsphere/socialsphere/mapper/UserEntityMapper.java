package com.socialsphere.socialsphere.mapper;

import com.socialsphere.socialsphere.entity.CommentEntity;
import com.socialsphere.socialsphere.entity.PersonalChatEntity;
import com.socialsphere.socialsphere.entity.PostEntity;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.payload.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserEntityMapper {
    public UserDto getUserResponseDto(UserEntity userEntity) {
        log.info("Converting UserEntity to UserDto in UserEntityMapper, getUserResponseDto method");
        return UserDto.builder()
                .username(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .post(getPost(userEntity.getPostEntity()))
                .savedPost(getPost(userEntity.getSavedPostEntity()))
                .followers(getUserFollowersAndFollowing(userEntity.getFollowers()))
                .following(getUserFollowersAndFollowing(userEntity.getFollowing()))
                .profilePicture(userEntity.getProfilePicture())
                .gender(userEntity.getGender())
                .personalChatDto(getPersonalChatDto(userEntity.getPersonalChats()))
                .build();
    }

    private List<List<PersonalChatDto>> getPersonalChatDto(List<List<PersonalChatEntity>> personalChatsEntity) {
        return personalChatsEntity.stream()
                .map(innerList -> innerList.stream()
                        .map(this::personalChatEntityToPersonalChatDto)
                        .toList())
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
                .map(this::toBasicUser)
                .toList();
    }

    private List<PostDto> getPost(List<PostEntity> postEntity) {
        return postEntity.stream()
                .map(post -> PostDto.builder()
                        .url(post.getUrl())
                        .caption(post.getCaption())
                        .type(post.getType())
                        .like(post.getLike().stream()
                                .map(this::toBasicUser)
                                .toList())
                        .commentDto(post.getComment().stream()
                                .map(this::toCommentDto
                                )
                                .toList()
                        )
                        .build())
                .toList();
    }

    private BasicUserInfoDto toBasicUser(UserEntity user) {
        return BasicUserInfoDto.builder()
                .username(user.getUsername())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    private CommentDto toCommentDto(CommentEntity comment) {
        return CommentDto.builder()
                .basicUserInfoDto(BasicUserInfoDto.builder()
                        .username(comment.getUserEntity().getUsername())
                        .profilePicture(comment.getUserEntity().getProfilePicture())
                        .build())
                .content(comment.getContent())
                .build();
    }

}
