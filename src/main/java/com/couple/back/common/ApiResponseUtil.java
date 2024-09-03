package com.couple.back.common;

import com.couple.back.common.CommonConstants.ResultStatus;


public class ApiResponseUtil {
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ResultStatus.Success, message, data);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(ResultStatus.Fail, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ResultStatus.Error, message, null);
    }
}
