package com.couple.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.StatusConverter;
import com.couple.back.common.StatusConverter.CoupleStatusType;
import com.couple.back.common.StatusConverter.GenderType;
import com.couple.back.dto.CoupleStatusDetail;
import com.couple.back.dto.CoupleStatusResponse;
import com.couple.back.model.User;
import com.couple.back.mybatis.CoupleMapper;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.CoupleService;

import org.apache.commons.lang3.StringUtils;

@Service
public class CoupleServiceImpl implements CoupleService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CoupleMapper coupleMapper;
    
    public CoupleStatusResponse getStatusType(Long userId) throws Exception {
        if(userId == null)
            throw new IllegalArgumentException("Parameter is Empty");
        
        User user = userMapper.selectDataByUserId(userId);
        if(user == null || user.getCoupleId() == null)
            return null;

        Long coupleId = user.getCoupleId();
        CoupleStatusDetail detail = coupleMapper.selectCoupleStatusDetailByCoupleId(coupleId);

        if(detail == null) {
            return null;
        }

        GenderType genderType = StatusConverter.getGenderType(user);
        CoupleStatusType statusType =  StatusConverter.getCoupleStatusType(detail.getStatus());
        String senderYn = detail.getUpdateUserId() == userId ? "Y" : "N";
        String other = genderType == GenderType.MAN ? detail.getWomanName() : detail.getManName();
        String messageType = StringUtils.equals(senderYn, "Y") ? "님에게" : "님이";
        String message = "";

        switch (statusType) {
            case REQUEST:
                message = "[ " + other + " ] " + messageType + " 커플 신청을 요청하였습니다.";
                break;
            case REJECT:
                message = "[ " + other + " ] " + messageType + " 커플 신청을 거절하였습니다.";
                break;
            case APPROVAL: 
                message = "[ " + other + " ] " + messageType + " 커플 신청을 승인하였습니다.";
                break;
            case BREAKUP: // 수락 후 확인을 거쳐 정식 커플이 된 상태
                message = "[ " + other + " ] " + messageType + " 이별을 요청하였습니다.";
                break;
            case TERMINATED: // 사용자가 이별을 선언한 상태 => 상대방이 확인 하면 종료상태로 변경됨
                message = "[ " + other + " ] " + messageType + " 이별을 승인하였습니다.";
                break;
            default:
                return null;
        }

        return new CoupleStatusResponse(statusType, senderYn, message, coupleId);
    }
}
