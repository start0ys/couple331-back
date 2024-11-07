package com.couple.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.dto.CoupleStatusDetail;
import com.couple.back.dto.CoupleStatusRequest;
import com.couple.back.dto.CoupleStatusResponse;
import com.couple.back.model.Couple;
import com.couple.back.service.CoupleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/couple")
public class CoupleController {

    @Autowired
    private CoupleService coupleService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Couple>> register(@RequestBody Couple saveData) {
        try {
            Couple couple = coupleService.registerCouple(saveData);
            if(couple == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, couple), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : register / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : register / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<CoupleStatusResponse>> getStatusType(@PathVariable Long userId) {
        try {
            CoupleStatusResponse result = coupleService.getStatusType(userId);
            if(result == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : getStatusType / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : getStatusType / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{coupleId}/status")
    public ResponseEntity<ApiResponse<String>> updateCoupleStatus(@PathVariable Long coupleId,  @RequestBody CoupleStatusRequest coupleStatusRequest)  {
        try {
            ResultStatus result = coupleService.updateCoupleStatus(coupleId, coupleStatusRequest);
            if (result == ResultStatus.FAIL) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : updateCoupleStatus / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : updateCoupleStatus / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{coupleId}/detail")
    public ResponseEntity<ApiResponse<CoupleStatusDetail>> getCoupleDetail(@PathVariable Long coupleId) {
        try {
            CoupleStatusDetail result = coupleService.getCoupleDetail(coupleId);
            if(result == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : getCoupleDetail / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : getCoupleDetail / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{coupleId}/desc")
    public ResponseEntity<ApiResponse<String>> updateCoupleDesc(@PathVariable Long coupleId,  @RequestBody CoupleStatusRequest coupleStatusRequest)  {
        try {
            ResultStatus result = coupleService.updateCoupleDesc(coupleId, coupleStatusRequest);
            if (result == ResultStatus.FAIL) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Status : " + HttpStatus.BAD_REQUEST + " / Method : updateCoupleDesc / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Status : " + HttpStatus.INTERNAL_SERVER_ERROR + " / Method : updateCoupleDesc / Message : " + e.getMessage());
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
