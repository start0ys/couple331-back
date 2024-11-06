package com.couple.back.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.SHA256;
import com.couple.back.model.User;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.UserService;

import com.couple.back.common.StatusConverter;
import com.couple.back.common.StatusConverter.GenderType;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserMapper userMapper;
    
    public User registerUser(User user) throws Exception {
        if(user == null) 
            throw new IllegalArgumentException("Parameter is Empty");

        String salt = SHA256.getSalt();

        user.setSalt(salt);
        user.setPassword(SHA256.getEncrypt(user.getPassword(), salt));

        if(StringUtils.isAnyEmpty(user.getEmail(), user.getSalt(), user.getPassword(), user.getGender(), user.getNickname(), user.getName())) 
            return null;

        user.setStatus("01");

        
        userMapper.insertData(user);

        return user;
    }

    public User getUser(Long userId) throws Exception{
        if(userId == null) 
            throw new IllegalArgumentException("Parameter is Empty");

        return userMapper.selectDataByUserId(userId);
    }

    public List<User> getOppositeGenderSingleUsers(String gender) throws Exception {
        if(StringUtils.isEmpty(gender))
            throw new IllegalArgumentException("Parameter is Empty");

        GenderType genderType = StatusConverter.getGenderType(gender);

        if(genderType == null)
            return null;

        String oppositeGender = genderType == GenderType.MAN ? "02" : "01";

        return userMapper.selectSingleUsersByGender(oppositeGender);
    }

    public void updateCoupleId(Long userId, Long coupleId) throws Exception {
        if(userId == null)
            throw new IllegalArgumentException("Parameter is Empty");
        
        User user = new User();
        user.setUserId(userId);
        user.setCoupleId(coupleId);

        userMapper.updateCoupleId(user);
    }
}
