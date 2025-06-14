package com.socialsphere.socialsphere.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse<T> {
    private String success;
    private String message;
    private T data;
    private Object metadata;
}
