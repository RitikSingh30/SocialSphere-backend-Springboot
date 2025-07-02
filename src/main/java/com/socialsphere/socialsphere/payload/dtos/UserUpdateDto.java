package com.socialsphere.socialsphere.payload.dtos;

import com.socialsphere.socialsphere.utility.Gender;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    private String bio;
    private Gender gender;
    private String profilePicture;
}
