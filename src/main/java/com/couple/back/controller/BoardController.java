package com.couple.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.dto.BoardDetail;
import com.couple.back.dto.BoardDetailResponse;
import com.couple.back.model.Board;
import com.couple.back.model.BoardComment;
import com.couple.back.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {
    
    @Autowired
    private BoardService boardService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<BoardDetail>>> getBoardList(@RequestParam(value = "searchWord", required = false, defaultValue="") String searchWord 
        , @RequestParam(value = "searchType", required = false, defaultValue="title") String searchType
        , @RequestParam(value = "includeClobDataYn", required = false, defaultValue="N") String includeClobDataYn
        , final Pageable pageable) {
        try {
            Page<BoardDetail> result = boardService.getBoardList(searchType, searchWord, includeClobDataYn, pageable);
            if(result == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : getBoardList / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : getBoardList / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getBoardDetail(@PathVariable Long boardId, final Pageable pageable) {
        try {
            BoardDetailResponse result = boardService.getBoardDetail(boardId, pageable);
            if(result == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : getBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : getBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Board>> registerBoard(@RequestBody Board saveData)  {
        try {
            Board board = boardService.registerBoard(saveData);
            if(board == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, board), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : registerBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : registerBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<ApiResponse<String>> updateBoard(@PathVariable Long boardId, @RequestBody Board saveData)  {
        try {
            boardService.updateBoard(boardId, saveData);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : updateBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : updateBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<String>> deleteBoard(@PathVariable Long boardId)  {
        try {
            boardService.deleteBoard(boardId);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : deleteBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : deleteBoard / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{boardId}/comment/register")
    public ResponseEntity<ApiResponse<String>> registerComment(@PathVariable Long boardId, @RequestBody BoardComment saveData)  {
        try {
            boardService.registerComment(boardId, saveData);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : registerComment / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : registerComment / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<String>> updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody BoardComment saveData)  {
        try {
            boardService.updateComment(commentId, saveData);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : updateComment / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : updateComment / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId)  {
        try {
            boardService.deleteComment(commentId);
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : deleteComment / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : deleteComment / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
