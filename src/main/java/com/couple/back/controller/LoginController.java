package com.couple.back.controller;

import java.lang.Exception;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
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
}
