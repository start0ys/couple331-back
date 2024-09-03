package com.couple.back.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.couple.back.model.MailRequest;

@Mapper
@Repository
public interface MailMapper {
    public void insertData(MailRequest mailRequest);
    public void deleteData(MailRequest mailRequest);
    public int selectCountByVerificationCode(MailRequest mailRequest);
    
}
