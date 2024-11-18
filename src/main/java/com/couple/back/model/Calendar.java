package com.couple.back.model;

import com.couple.back.common.Auditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Calendar extends Auditable{
    private String id;
    private String title;
    private String startDay;
    private String endDay;
    private String color;
    private String textColor;
    private String type;
    private Long coupleId;
    private Long userId;
}
