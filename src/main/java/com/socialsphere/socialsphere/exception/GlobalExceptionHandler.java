package com.socialsphere.socialsphere.exception;

import com.socialsphere.socialsphere.payload.response.ApiResponse;
import com.socialsphere.socialsphere.payload.response.exception.ApiExceptionDto;
import com.socialsphere.socialsphere.payload.response.SendOtpResponseDto;
import com.socialsphere.socialsphere.payload.response.exception.UnauthorizedResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.socialsphere.socialsphere.utility.CommonUtil.getErrorApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String,String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("Error occurred while validating request", methodArgumentNotValidException);
        Map<String,String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName;
            try{
                fieldName = ((FieldError) error).getField();
            } catch(ClassCastException e){
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(getErrorApiResponse("Field validation error",errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleOtpException(OtpException otpException){
        log.error("OTP exception error occurred", otpException);
        Map<String,Object> errors = new HashMap<>();
        errors.put("Title","OTP Error");
        errors.put("Timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(getErrorApiResponse(otpException.getMessage(),errors), otpException.getStatusCode());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<UnauthorizedResponseDto> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException){
        return new ResponseEntity<>(new UnauthorizedResponseDto(usernameNotFoundException.getMessage(),false), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<UnauthorizedResponseDto> handleTokenException(TokenException tokenException){
        return new ResponseEntity<>(new UnauthorizedResponseDto(tokenException.getMessage(),false), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<UnauthorizedResponseDto> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException){
        return new ResponseEntity<>(new UnauthorizedResponseDto(userAlreadyExistException.getMessage(),false), userAlreadyExistException.getStatusCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<UnauthorizedResponseDto> handleBadCredentialsException(BadCredentialsException badCredentialsException){
        return new ResponseEntity<>(new UnauthorizedResponseDto("Username or password is incorrect",false), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDto> handleUncheckedException(Exception e){
        log.error("Unhandled exception", e);
        return new ResponseEntity<>(new ApiExceptionDto("Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
