package com.socialsphere.socialsphere.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    public ApiException(Exception exception) {
        super(exception);
    }
}
