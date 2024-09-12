package com.couple.back.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.couple.back.model.User;

@Mapper
public interface UserMapper {
    public void insertData(User user);
    public User selectDataByEmail(String email);
    public User selectDataByUserId(Long userId);
    public int selectCountByEmail(String email);
    public void updateDataByEmail(User user);
}
