package com.couple.back.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.couple.back.dto.CoupleStatusDetail;

@Mapper
public interface CoupleMapper {
    public CoupleStatusDetail selectCoupleStatusDetailByCoupleId(Long coupleId);
}
