package com.couple.back.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.model.User;
import com.couple.back.service.LoginService;


@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    
	@PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginData) {
        ResponseEntity<Map<String, Object>> rs = null;

		try{
			if (loginData != null) {
				Map<String, Object> result = loginService.loginUser(loginData);
				if (result != null) {
					rs = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
				}
			}
			if (rs == null)
                rs = new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			rs = new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
		return rs;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // 로그아웃 로직
		return null;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<User> resetPassword(@RequestBody User user) {
        // 패스워드 초기화 로직
		return null;
    }
}
