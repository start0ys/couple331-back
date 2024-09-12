package com.couple.back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.model.Couple;

@RestController
@RequestMapping("/couple")
public class CoupleController {

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Couple>> register(@RequestBody Couple saveData) {
        try {
            Couple couple = null;
            if(couple == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, couple), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{coupleId}")
    public ResponseEntity<ApiResponse<Couple>> getCouple(@PathVariable Long coupleId) {
        // 커플 정보 조회
        return null;
    }

    @PutMapping("/{coupleId}")
    public ResponseEntity<ApiResponse<Couple>> updateCouple(@PathVariable Long coupleId, @RequestBody Couple saveData) {
        // 커플 정보 수정
        return null;
    }

    @DeleteMapping("/{coupleId}")
    public ResponseEntity<ApiResponse<Couple>> deleteCouple(@PathVariable Long coupleId) {
        // 커플 삭제
        return null;
    }
}
