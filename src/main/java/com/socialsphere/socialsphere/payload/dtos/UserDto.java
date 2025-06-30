package com.socialsphere.socialsphere.payload.dtos;

import com.socialsphere.socialsphere.utility.Gender;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String bio;
    private List<PostDto> post;
    private List<PostDto> savedPost;
    private List<BasicUserInfoDto> followers;
    private List<BasicUserInfoDto> following;
    private String profilePicture;
    private Gender gender;
    private List<List<PersonalChatDto>> personalChatDto;
}
