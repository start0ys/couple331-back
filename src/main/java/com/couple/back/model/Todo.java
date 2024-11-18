package com.couple.back.model;

import com.couple.back.common.Auditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Todo extends Auditable{
    private String id;
    private String todo; 
    private String completedYn;
    private String day;
    private int dayOrder;
}
