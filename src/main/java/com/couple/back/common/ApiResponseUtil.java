package com.couple.back.common;

import com.couple.back.common.CommonConstants.ResponseStatus;


public class ApiResponseUtil {
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ResponseStatus.Success, message, data);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(ResponseStatus.Fail, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ResponseStatus.Error, message, null);
    }
}
