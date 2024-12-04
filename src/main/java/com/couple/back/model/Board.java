package com.couple.back.model;

import com.couple.back.common.Auditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board extends Auditable{
    private Long boardId;
    private String category;
    private String title;
    private String content;
}
