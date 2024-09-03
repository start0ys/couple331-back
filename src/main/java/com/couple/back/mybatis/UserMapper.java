package com.couple.back.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.couple.back.model.User;

@Mapper
@Repository
public interface UserMapper {
    public void saveUser(User user);
    public User getUserByEmail(String email);
    public int emailCheck(String email);
    public void updateUserByEmail(User user);
}
