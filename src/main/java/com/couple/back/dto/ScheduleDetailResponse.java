package com.couple.back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDetailResponse {
    private String type;
    private String title;
    private String completedYn;
}
