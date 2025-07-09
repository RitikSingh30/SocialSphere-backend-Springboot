package com.socialsphere.socialsphere.mapper;

import com.socialsphere.socialsphere.entity.CommentEntity;
import com.socialsphere.socialsphere.entity.PostEntity;
import com.socialsphere.socialsphere.entity.UserEntity;
import com.socialsphere.socialsphere.payload.dtos.CommentDto;
import com.socialsphere.socialsphere.payload.dtos.CompletePostDetailDto;
import com.socialsphere.socialsphere.payload.dtos.PostDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class PostMapper {
    private PostMapper() {}

    public static List<CompletePostDetailDto> mapPostThatUserHasPermissionToSee(UserEntity userEntity){
        log.info("Mapping post the user has permission to see to list of complete post details dto in PostMapper.mapPostThatUserHasPermissionToSee");
        List<CompletePostDetailDto> completePostDetailDtos = new ArrayList<>();
        completePostDetailDtos.add(getCompletePostDetailDto(userEntity));
        userEntity.getFollowing().stream()
                .filter(Objects::nonNull)
                .map(PostMapper::getCompletePostDetailDto)
                .forEach(completePostDetailDtos::add);
        return completePostDetailDtos;
    }

    public static CompletePostDetailDto getCompletePostDetailDto(UserEntity userEntity){
        log.info("Mapping UserEntity post to CompletePostDetailDto in PostMapper.getCompletePostDetailDto");
        return CompletePostDetailDto.builder()
                .basicUserInfoDto(BasicUserInfoMapper.getBasicUserDto(userEntity))
                .postDto(PostMapper.getListPostDto(userEntity.getPostEntity()))
                .build();
    }

    public static List<PostDto> getListPostDto(List<PostEntity> postEntity) {
        log.info("Mapping list of postEntity to list of PostDto in PostMapper.getListPostDto");
        return postEntity.stream()
                .filter(Objects::nonNull)
                .map(PostMapper::getPostDto)
                .toList();
    }

    public static PostDto getPostDto(PostEntity postEntity){
        log.info("Mapping postEntity to PostDto in PostMapper.getPostDto");
        return PostDto.builder()
                .url(postEntity.getUrl())
                .caption(postEntity.getCaption())
                .type(postEntity.getType())
                .like(postEntity.getLike().stream()
                        .filter(Objects::nonNull)
                        .map(BasicUserInfoMapper::getBasicUserDto)
                        .toList())
                .commentDto(postEntity.getComment().stream()
                        .filter(Objects::nonNull)
                        .map(PostMapper::getCommentDto)
                        .toList())
                .build();
    }

    public static CommentDto getCommentDto(CommentEntity commentEntity){
        log.info("Mapping commentEntity to CommentDto in CommentMapper.getCommentDto");
        return CommentDto.builder()
                .basicUserInfoDto(BasicUserInfoMapper.getBasicUserDto(commentEntity.getUserEntity()))
                .content(commentEntity.getContent())
                .build();
    }
}
