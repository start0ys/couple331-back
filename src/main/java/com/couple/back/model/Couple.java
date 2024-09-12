package com.couple.back.model;

import java.time.LocalDateTime;

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
    private LocalDateTime startDate;
}