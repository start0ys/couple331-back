package com.couple.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.SHA256;
import com.couple.back.model.User;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.UserService;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserMapper userMapper;
    
    public User registerUser(User user) throws Exception {
        if(user == null) return null;

        String salt = SHA256.getSalt();

        user.setSalt(salt);
        user.setPassword(SHA256.getEncrypt(user.getPassword(), salt));


        if(user.validation(true)) 
            return null;

        
        userMapper.insertData(user);

        return user;
    }
}
