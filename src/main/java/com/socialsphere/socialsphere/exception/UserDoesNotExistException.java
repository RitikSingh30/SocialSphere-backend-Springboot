package com.socialsphere.socialsphere.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class UserDoesNotExistException extends RuntimeException {
    private final HttpStatusCode statusCode;
    public UserDoesNotExistException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
