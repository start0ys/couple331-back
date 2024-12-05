package com.couple.back.dto;

import com.couple.back.model.BoardComment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCommentDetail extends BoardComment{
    private String author;
    private String parentAuthor;
}
