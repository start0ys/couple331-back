package com.couple.back.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.couple.back.dto.BoardCommentDetail;
import com.couple.back.dto.BoardDetail;
import com.couple.back.dto.BoardDetailResponse;
import com.couple.back.model.Board;
import com.couple.back.model.BoardComment;

public interface BoardService {
    public Page<BoardDetail> getBoardList(String searchType, String searchWord, String includeClobDataYn, Pageable pageable) throws Exception;
    public BoardDetailResponse getBoardDetail(Long boardId, Pageable pageable) throws Exception;
    public Board registerBoard(Board board) throws Exception;
    public void updateBoard(Long boardId, Board board) throws Exception;
    public void deleteBoard(Long boardId) throws Exception;

    public Page<BoardCommentDetail> getBoardCommentList(Long boardId, Pageable pageable) throws Exception;
    public BoardComment registerComment(Long boardId, BoardComment comment) throws Exception;
    public void updateComment(Long commentId, BoardComment comment) throws Exception;
    public void deleteComment(Long commentId) throws Exception;
}
