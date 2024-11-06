package com.couple.back.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.couple.back.common.Auditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Couple extends Auditable {
    private Long coupleId;
    private Long manId;
    private Long womanId;
    private String coupleDesc;
    private String status;
    private LocalDateTime startDate;

    public void setStartDate(String date) {
        this.startDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
    }
}