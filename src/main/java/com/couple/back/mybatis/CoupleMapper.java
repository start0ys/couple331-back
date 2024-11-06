package com.couple.back.mybatis;

import org.apache.ibatis.annotations.Mapper;

import com.couple.back.dto.CoupleStatusDetail;

import com.couple.back.model.Couple;

@Mapper
public interface CoupleMapper {
    public CoupleStatusDetail selectCoupleStatusDetailByCoupleId(Long coupleId);
    public void insertData(Couple couple);
    public Couple selectCoupleByCoupleId(Long coupleId);
    public void deleteCoupleByCoupleId(Long coupleId);
    public void updateCoupleStatus(Couple couple);
}
