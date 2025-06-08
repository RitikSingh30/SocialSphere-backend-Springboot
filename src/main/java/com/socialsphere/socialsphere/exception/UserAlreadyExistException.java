package com.socialsphere.socialsphere.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class UserAlreadyExistException extends RuntimeException {
    private final HttpStatusCode statusCode;
    public UserAlreadyExistException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
