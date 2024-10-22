package com.couple.back.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.model.MailAuthRequest;
import com.couple.back.model.User;
import com.couple.back.service.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

	@PostMapping("/sendCode")
    public ResponseEntity<ApiResponse<String>> sendCode(@RequestBody MailAuthRequest mailAuthRequest) throws Exception {
		try{
			ApiResponse<String> result = authService.sendCode(mailAuthRequest);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);   
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/verifyCode")
    public ResponseEntity<ApiResponse<String>> verifyCode(@RequestBody MailAuthRequest mailAuthRequest) throws Exception {
		try{
			ApiResponse<String> result = authService.verifyCode(mailAuthRequest);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);   
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
	@PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User loginData) {
		try{
			ApiResponse<String> result = authService.loginUser(loginData);
			if (result.getStatus() == ResultStatus.FAIL) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			} 

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
            return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.PARAM_ERROR_MESSAGE),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // 로그아웃 로직
		return null;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody User user) {
        // 패스워드 초기화 로직
		return null;
    }
}
