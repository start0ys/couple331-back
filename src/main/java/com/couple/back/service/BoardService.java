package com.couple.back.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.couple.back.dto.BoardDetailResponse;
import com.couple.back.model.Board;
import com.couple.back.model.BoardComment;

public interface BoardService {
    public Page<Board> getBoardList(String searchType, String searchWord, String includeClobDataYn, Pageable pageable) throws Exception;
    public BoardDetailResponse getBoardDetail(Long boardId, Pageable pageable) throws Exception;
    public void registerBoard(Board board) throws Exception;
    public void updateBoard(Long boardId, Board board) throws Exception;
    public void deleteBoard(Long boardId) throws Exception;

    public void registerComment(Long boardId, BoardComment comment) throws Exception;
    public void updateComment(Long commentId, BoardComment comment) throws Exception;
    public void deleteComment(Long commentId) throws Exception;
}
