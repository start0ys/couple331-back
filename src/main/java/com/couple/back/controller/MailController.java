package com.couple.back.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.model.MailRequest;
import com.couple.back.service.MailService;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;
    
	@PostMapping("/sendVerificationCode")
    public ResponseEntity<ApiResponse<String>> sendVerificationCode(@RequestBody MailRequest mailRequest) throws Exception {
		try{
			String result = mailService.sendVerificationCode(mailRequest);
			if (StringUtils.isEmpty(result)) {
				return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST);   
			}
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/verifyCode")
    public ResponseEntity<ApiResponse<ResultStatus>> verifyCode(@RequestBody MailRequest mailRequest) throws Exception {
		try{
			ResultStatus result = mailService.verifyCode(mailRequest);
			if (result == ResultStatus.Fail) {
				return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST);   
			}
			return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, result), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
