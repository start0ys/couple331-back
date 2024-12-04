package com.couple.back.model;

import com.couple.back.common.Auditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardComment extends Auditable{
    private Long commentId;
    private Long parentId;
    private String content;
    private Long boardId;
}
