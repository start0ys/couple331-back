package com.couple.back.common;

import com.couple.back.common.CommonConstants.ResultStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // JSON으로 직렬화 할 때 필요
@AllArgsConstructor
public class ApiResponse<T> {
    private final ResultStatus status;
    private final String message;
    private final T data;
}
