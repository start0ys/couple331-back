package com.couple.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.couple.back.model.User;
import com.couple.back.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User saveData) {
        try {
            User user = userService.registerUser(saveData);
            if(user == null) {
                return new ResponseEntity<>(ApiResponseUtil.fail(CommonConstants.FAIL_MESSAGE),HttpStatus.BAD_REQUEST); 
            }
            return new ResponseEntity<>(ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponseUtil.error(CommonConstants.ERROR_MESSAGE),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable Long userId) {
        // 사용자 정보 조회
        return null;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long userId, @RequestBody User saveData) {
        // 사용자 정보 수정
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> deleteUser(@PathVariable Long userId) {
        // 사용자 삭제
        return null;
    }
}
