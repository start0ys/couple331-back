package com.couple.back.dto;

import com.couple.back.model.Board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDetail extends Board{
    private String author;
}
