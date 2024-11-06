package com.couple.back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoupleStatusRequest {
    Long updateUserId;
    String approvalStatusType;
}
