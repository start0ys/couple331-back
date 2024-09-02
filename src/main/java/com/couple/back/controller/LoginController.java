package com.couple.back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couple.back.model.User;


@RestController
@RequestMapping("/auth")
public class LoginController {
    
    @GetMapping("/")
	public ResponseEntity<Map<String, Object>> test() throws Exception {
		ResponseEntity<Map<String, Object>> rs = null;

		try{
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", "test");
            rs = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			rs = new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
		return rs;
	}

	@PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        // 로그인 로직
		return null;
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
