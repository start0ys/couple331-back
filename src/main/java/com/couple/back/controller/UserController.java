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

import com.couple.back.model.User;
import com.couple.back.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User saveData) {
        ResponseEntity<User> rs = null;

        try {
            User user = userService.registerUser(saveData);
            if(user != null) {
                rs = new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                rs = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);    
            }
        } catch (Exception e) {
            rs = new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        // 사용자 정보 조회
        return null;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User saveData) {
        // 사용자 정보 수정
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        // 사용자 삭제
        return null;
    }
}
