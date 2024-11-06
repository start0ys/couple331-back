package com.couple.back.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.couple.back.common.CommonConstants.DateType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class CommonUtil {
    
    public static long convertToSeconds(int timeValue, DateType type, boolean returnInMilliseconds) {
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

    public static LocalDateTime convertStringToLocalDate(String date, String pattern) {
        return StringUtils.isAnyEmpty(date, pattern) ? null 
            : LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
    }
    
    public static String calculateDaysFromToday(LocalDateTime startDateTime, boolean isStartDateInclusive, DateType type) {
        return startDateTime == null ? ""
            : calculateDateDifference(startDateTime.atZone(ZoneId.of("Asia/Seoul")).toLocalDate(), getCurrentDate(), isStartDateInclusive, type);
    }

    public static LocalDate getCurrentDate() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
    }

    public static String calculateDateDifference(LocalDate startDate, LocalDate endDate, boolean isStartDateInclusive, DateType type) {
        if (startDate == null || endDate == null)
            return "";

        long difference;
        switch (type) {
            case DAYS:
                difference = ChronoUnit.DAYS.between(startDate, endDate);
                break;
            case MONTHS:
                difference = ChronoUnit.MONTHS.between(startDate, endDate);
                break;
            case YEARS:
                difference = ChronoUnit.YEARS.between(startDate, endDate);
                break;
            default:
                return "";
        }
        return Long.toString(difference + (isStartDateInclusive ? 1 : 0));
    }
}
