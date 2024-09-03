package com.couple.back.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.SHA256;
import com.couple.back.model.User;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
    
    @Autowired
    UserMapper userMapper;

    public Map<String, Object> loginUser(User loginData) throws Exception {
        if(loginData == null) return null;

        String email = loginData.getEmail();
        String password = loginData.getPassword();

        if(StringUtils.isAnyEmpty(email, password)) return null;

        User user = userMapper.getUserByEmail(email);

        String result = "fail";
        String msg = "";
        // String token = "";
        //int exprTime = 3600000;


        if(user == null) {
            msg = "존재하지 않는 아이디입니다.";
        } else {
            String salt = user.getSalt();
            String dbPw = user.getPassword();
            String encryptPw = SHA256.getEncrypt(password, salt);
            if(StringUtils.equals(dbPw, encryptPw)) {
                result = "success";
                // token = tokenProvider.createToken(user);
            } else {
                msg = "비밀번호를 확인해주세요.";
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", result);
        resultMap.put("msg", msg);
        // resultMap.put("token", token);

        
    
        return resultMap;
    }
}
