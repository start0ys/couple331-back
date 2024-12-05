package com.couple.back.dto;

import org.springframework.data.domain.Page;

import com.couple.back.model.Board;
import com.couple.back.model.BoardComment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardDetailResponse {
    private BoardDetail board;
    private Page<BoardCommentDetail> comments;
}
