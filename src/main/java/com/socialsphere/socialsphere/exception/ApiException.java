package com.socialsphere.socialsphere.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final Exception exception;
    public ApiException(Exception exception) {
        this.exception = exception;
    }
}
