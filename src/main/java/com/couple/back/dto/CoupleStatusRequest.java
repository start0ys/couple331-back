package com.couple.back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoupleStatusRequest {
    private Long updateUserId;
    private String approvalStatusType;
    private String coupleDesc;
}
