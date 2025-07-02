package com.socialsphere.socialsphere.exception;

import com.mongodb.MongoException;
import com.socialsphere.socialsphere.payload.response.ApiResponse;
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
        return new ResponseEntity<>(getErrorApiResponse(otpException.getMessage(),getErrors("OTP Error")), otpException.getStatusCode());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException){
        log.error("UsernameNotFoundException error occurred", usernameNotFoundException);
        return new ResponseEntity<>(getErrorApiResponse(usernameNotFoundException.getMessage(),getErrors("Username not found")), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleTokenException(TokenException tokenException){
        log.error("TokenException error occurred", tokenException);
        return new ResponseEntity<>(getErrorApiResponse(tokenException.getMessage(),getErrors("Token Exception")), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleUserAlreadyExistException(UserAlreadyExistException userAlreadyExistException){
        log.error("UserAlreadyExistException error occurred", userAlreadyExistException);
        return new ResponseEntity<>(getErrorApiResponse(userAlreadyExistException.getMessage(),getErrors("User already exist")), userAlreadyExistException.getStatusCode());
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleUserAlreadyExistException(UserDoesNotExistException userDoesNotExistException){
        log.error("userDoesNotExistException error occurred", userDoesNotExistException);
        return new ResponseEntity<>(getErrorApiResponse(userDoesNotExistException.getMessage(),getErrors("User already exist")), userDoesNotExistException.getStatusCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleBadCredentialsException(BadCredentialsException badCredentialsException){
        log.error("BadCredentialsException error occurred", badCredentialsException);
        return new ResponseEntity<>(getErrorApiResponse("Username or password is incorrect",getErrors("Bad credentials")), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleMongoException(MongoException mongoException){
        return new ResponseEntity<>(getErrorApiResponse(mongoException.getMessage(),getErrors("Exception")), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleMongoException(ApiException apiException){
        log.error("ApiException error occurred", apiException);
        return new ResponseEntity<>(getErrorApiResponse(apiException.getMessage(),getErrors("API Exception")), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String,Object>>> handleUncheckedException(Exception e){
        log.error("Unhandled exception", e);
        return new ResponseEntity<>(getErrorApiResponse(e.getMessage(),getErrors("Exception")), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String,Object> getErrors(String title){
        Map<String,Object> errors = new HashMap<>();
        errors.put("title",title);
        errors.put("timeStamp",LocalDateTime.now().toString());
        return errors;
    }

}
