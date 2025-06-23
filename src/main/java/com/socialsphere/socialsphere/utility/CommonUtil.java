package com.socialsphere.socialsphere.utility;

import com.socialsphere.socialsphere.payload.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommonUtil {
    private CommonUtil() {}

    public static Map<String,Object> getMetadata(HttpServletRequest request) {
        return Map.of("timestamp", Instant.now().toString(), "URI", request.getRequestURI());
    }

    public static Map<String,Object> prepareOtpDataResponse(String otp, String email){
        log.info("preparing data response for successful for successful otp send");
        Map<String,Object> data = new HashMap<>();
        data.put("otp",otp);
        data.put("expireIn","300ms");
        data.put("deliveryMethod","email");
        data.put("deliveredTo",email);
        return data;
    }

    public static <T> ApiResponse<T> getSuccessApiResponse(String message, T data, HttpServletRequest request){
        return ResponseUtil.success(message,data,CommonUtil.getMetadata(request));
    }

    public static <T> ApiResponse<T> getErrorApiResponse(String message, T data){
        return ResponseUtil.error(message,data);
    }
}
