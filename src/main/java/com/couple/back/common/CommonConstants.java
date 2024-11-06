package com.couple.back.common;

public class CommonConstants {
    public static enum ResultStatus {SUCCESS, FAIL, ERROR};
    public static enum DateType {SECONDS, MINUTES, HOURS, DAYS, MONTHS, YEARS};

    public static final String SUCCESS_MESSAGE = "성공하였습니다.";
    public static final String FAIL_MESSAGE = "실패하였습니다.";
    public static final String ERROR_MESSAGE = "오류가 발생하였습니다.";
    public static final String PARAM_ERROR_MESSAGE = "파라미터가 정상적이지 않습니다.";
    public static final String TOKEN_EXPIRED_MESSAGE = "토큰이 만료되었습니다.";
}
