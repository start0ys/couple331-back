package com.couple.back.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.couple.back.dto.BoardCommentDetail;
import com.couple.back.dto.BoardDetail;
import com.couple.back.dto.BoardDetailResponse;
import com.couple.back.model.Board;
import com.couple.back.model.BoardComment;
import com.couple.back.mybatis.BoardMapper;
import com.couple.back.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService{
 
    @Autowired
    private BoardMapper boardMapper;

    public Page<BoardDetail> getBoardList(String searchType, String searchWord, String includeClobDataYn, Pageable pageable) throws Exception {

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("includeClobDataYn", includeClobDataYn);
        param.put("limit", limit);
        param.put("offset", offset);

        List<BoardDetail> datas = boardMapper.selectBoardDatas(param);
        int totalCount = boardMapper.totalBoardCount(param);

        return new PageImpl<>(datas, pageable, totalCount);
    }

    public BoardDetailResponse getBoardDetail(Long boardId, Pageable pageable) throws Exception {
        if(boardId == null)
            throw new IllegalArgumentException("Parameter is Empty");

        BoardDetail board = boardMapper.selectBoardData(boardId);
        if(board == null)
            throw new IllegalArgumentException("Board is Empty");

        Page<BoardCommentDetail> comments = getBoardCommentList(boardId, pageable);
        
        return new BoardDetailResponse(board, comments);
    }

    public Board registerBoard(Board board) throws Exception {
        if(board == null || StringUtils.isAnyEmpty(board.getCategory(), board.getTitle()) || board.getCreateUserId() == null) 
            throw new IllegalArgumentException("Parameter is Empty");

        boardMapper.insertBoard(board);

        return board;
    }

    public void updateBoard(Long boardId, Board board) throws Exception {
        if(board == null || boardId == null || board.getBoardId() != boardId || StringUtils.isAnyEmpty(board.getCategory(), board.getTitle())) 
            throw new IllegalArgumentException("Parameter is Empty");
        
        boardMapper.updateBoard(board);
    }

    public void deleteBoard(Long boardId) throws Exception {
        if(boardId == null)
            throw new IllegalArgumentException("Parameter is Empty");

        boardMapper.deleteBoard(boardId);
    }

    public Page<BoardCommentDetail> getBoardCommentList(Long boardId, Pageable pageable) throws Exception {

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        Map<String, Object> param = new HashMap<>();
        param.put("boardId", boardId);
        param.put("limit", limit);
        param.put("offset", offset);

        List<BoardCommentDetail> datas = boardMapper.selectCommentDatas(param);
        int totalCount = boardMapper.totalCommentCount(boardId);

        return new PageImpl<>(datas, pageable, totalCount);
    }

    public BoardComment registerComment(Long boardId, BoardComment comment) throws Exception {
        if(comment == null || boardId == null || comment.getBoardId() != boardId || StringUtils.isEmpty(comment.getContent())) 
            throw new IllegalArgumentException("Parameter is Empty");

        boardMapper.insertComment(comment);

        return comment;
    }

    public void updateComment(Long commentId, BoardComment comment) throws Exception {
        if(comment == null || commentId == null || comment.getCommentId() != commentId || StringUtils.isEmpty(comment.getContent())) 
            throw new IllegalArgumentException("Parameter is Empty");

        boardMapper.updateComment(comment);
    }

    public void deleteComment(Long commentId) throws Exception {
        if(commentId == null)
            throw new IllegalArgumentException("Parameter is Empty");

        boardMapper.deleteComment(commentId);
    }
}
