package com.couple.back.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoupleStatusDetail {
    private Long coupleId;
    private Long updateUserId;
    private String status;
    private String manName;
    private String womanName;
    private LocalDateTime startDate;
    private String coupleDesc;
}
