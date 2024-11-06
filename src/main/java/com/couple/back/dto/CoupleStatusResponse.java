package com.couple.back.dto;

import com.couple.back.common.StatusConverter.CoupleStatusType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoupleStatusResponse {
    private CoupleStatusType status;
    private String senderYn;
    private String message;
    private Long coupleId;
    private String daysTogether;
}
