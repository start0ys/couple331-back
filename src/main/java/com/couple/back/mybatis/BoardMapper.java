package com.couple.back.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.couple.back.dto.BoardCommentDetail;
import com.couple.back.dto.BoardDetail;
import com.couple.back.model.Board;
import com.couple.back.model.BoardComment;

@Mapper
public interface BoardMapper {
    public List<Board> selectBoardDatas(Map<String, Object> param);
    public int totalBoardCount(Map<String, Object> param);
    public BoardDetail selectBoardData(Long boardId);
    public List<BoardCommentDetail> selectCommentDatas(Map<String, Object> param);
    public int totalCommentCount(Long boardId);
    public void insertBoard(Board board);
    public void updateBoard(Board board);
    public void deleteBoard(Long boardId);

    public void insertComment(BoardComment comment);
    public void updateComment(BoardComment comment);
    public void deleteComment(Long commentId);
    public void deleteCommentByBoarId(Long boardId);
}
