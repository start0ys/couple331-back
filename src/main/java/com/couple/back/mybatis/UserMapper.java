package com.couple.back.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.couple.back.model.User;

@Mapper
@Repository
public interface UserMapper {
    public void insertData(User user);
    public User selectDataByEmail(String email);
    public int selectCountByEmail(String email);
    public void updateDataByEmail(User user);
}
