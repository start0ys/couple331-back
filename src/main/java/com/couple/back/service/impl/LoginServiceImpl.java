package com.couple.back.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.SHA256;
import com.couple.back.model.User;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
    
    @Autowired
    UserMapper userMapper;

    public ApiResponse<String> loginUser(User loginData) throws Exception {
        if(loginData == null) 
            return ApiResponseUtil.fail("로그인 데이터가 없습니다.");

        String email = loginData.getEmail();
        String password = loginData.getPassword();

        if(StringUtils.isAnyEmpty(email, password)) 
            return ApiResponseUtil.fail("이메일 또는 패스워드가 존재하지 않습니다.");

        User user = userMapper.getUserByEmail(email);


        // String token = "";
        //int exprTime = 3600000;


        if(user == null) {
            return ApiResponseUtil.fail("존재하지 않는 아이디입니다.");
        } else {
            String salt = user.getSalt();
            String dbPw = user.getPassword();
            String encryptPw = SHA256.getEncrypt(password, salt);
            if(StringUtils.equals(dbPw, encryptPw)) {
                // token = tokenProvider.createToken(user);
                // jwt로 token할지 고민중
                return ApiResponseUtil.success("성공하였습니다.", "");
            } else {
                return ApiResponseUtil.fail("비밀번호를 확인해주세요.");
            }
        }
    }
}
