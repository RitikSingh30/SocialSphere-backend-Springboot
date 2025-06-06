package com.socialsphere.socialsphere.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class OtpException extends RuntimeException {
    private final HttpStatusCode statusCode;
    public OtpException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
