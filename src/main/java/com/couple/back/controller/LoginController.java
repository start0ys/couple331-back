package com.couple.back.controller;

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
import com.couple.back.model.User;
import com.couple.back.service.LoginService;


@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    
	@PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User loginData) {
		if (loginData == null) {
			return new ResponseEntity<>(ApiResponseUtil.fail("로그인 데이터가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
		}

		try{
			ApiResponse<String> result = loginService.loginUser(loginData);
			if (result.getStatus() == ResultStatus.Fail) {
				return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			} 

			return new ResponseEntity<>(result, HttpStatus.OK);
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
