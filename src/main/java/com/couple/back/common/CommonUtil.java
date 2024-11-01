package com.couple.back.common;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class CommonUtil {
    
    public static long convertToSeconds(int timeValue, TimeUnit type, boolean returnInMilliseconds) {
        long result;
        switch (type) {
            case SECONDS:
                result = timeValue;
                break;
            case MINUTES:
                result = timeValue * 60;
                break;
            case HOURS:
                result =  timeValue * 3600;
                break;
            case DAYS:
                result = timeValue * 86400;
                break;
            default:
                result = timeValue;
                break;
        }

        // 밀리초로 반환할 경우, 1000을 곱함
        return returnInMilliseconds ? result * 1000 : result;
    }

     public static Map<String, Object> convertToMap(Object object) {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) 
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .convertValue(object, Map.class);
    }

    public static String getAccessToken(String authorizationHeader) {
        return StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : "";
    }

    public static String convertToJsonString(Object obj) {
        if(obj == null)
            return "";

        try {
            return new ObjectMapper()
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
