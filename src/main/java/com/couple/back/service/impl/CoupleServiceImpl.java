package com.couple.back.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.common.StatusConverter;
import com.couple.back.common.StatusConverter.ApprovalStatusType;
import com.couple.back.common.StatusConverter.CoupleStatusType;
import com.couple.back.common.StatusConverter.GenderType;
import com.couple.back.dto.CoupleStatusDetail;
import com.couple.back.dto.CoupleStatusRequest;
import com.couple.back.dto.CoupleStatusResponse;
import com.couple.back.model.Couple;
import com.couple.back.model.User;
import com.couple.back.mybatis.CoupleMapper;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.CoupleService;
import com.couple.back.service.UserService;

@Service
public class CoupleServiceImpl implements CoupleService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CoupleMapper coupleMapper;

    @Autowired
    private UserService userService;
    
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
            case BREAKUP: 
                message = "[ " + other + " ] " + messageType + " 이별을 요청하였습니다.";
                break;
            case TERMINATED: 
                message = "[ " + other + " ] " + messageType + " 이별을 승인하였습니다.";
                break;
            default:
                return null;
        }

        return new CoupleStatusResponse(statusType, senderYn, message, coupleId);
    }

    @Transactional
    public Couple registerCouple(Couple couple) throws Exception {
        if(couple == null || couple.getManId() == null || couple.getWomanId() == null || couple.getStartDate() == null || couple.getCreateUserId() == null) 
            throw new IllegalArgumentException("Parameter is Empty");
            
        coupleMapper.insertData(couple);

        Long coupleId = couple.getCoupleId();

        if (coupleId == null) {
            throw new IllegalStateException("Failed to generate couple ID");
        }

        userService.updateCoupleId(couple.getManId(), coupleId);
        userService.updateCoupleId(couple.getWomanId(), coupleId);

        return couple;
    }

    @Transactional
    public ResultStatus updateCoupleStatus(Long coupleId, CoupleStatusRequest coupleStatusRequest) throws Exception {
        if(coupleId == null || coupleStatusRequest == null || coupleStatusRequest.getUpdateUserId() == null)
            throw new IllegalArgumentException("Parameter is Empty");

        Couple coupleInfo = coupleMapper.selectCoupleByCoupleId(coupleId);

        if(coupleInfo == null || coupleInfo.getManId() == null || coupleInfo.getWomanId() == null || StringUtils.isEmpty(coupleInfo.getStatus()))
            return ResultStatus.FAIL;

        CoupleStatusType statusType =  StatusConverter.getCoupleStatusType(coupleInfo.getStatus());
        ApprovalStatusType approvalType = StatusConverter.getApprovalStatusType(coupleStatusRequest.getApprovalStatusType());

        coupleInfo.setUpdateUserId(coupleStatusRequest.getUpdateUserId());


        switch (statusType) {
            case REQUEST:
                coupleInfo.setStatus(approvalType == ApprovalStatusType.APPROVE ? "03" : "02");
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            case REJECT:
                coupleMapper.deleteCoupleByCoupleId(coupleId);
                userService.updateCoupleId(coupleInfo.getManId(), null);
                userService.updateCoupleId(coupleInfo.getWomanId(), null);
                return ResultStatus.SUCCESS;
            case APPROVAL: 
                coupleInfo.setStatus("04");
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            case BREAKUP: 
                coupleInfo.setStatus("05");
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            case TERMINATED: // 사용자가 이별을 선언한 상태 => 상대방이 확인 하면 종료상태로 변경됨
                coupleInfo.setStatus("03");
                coupleMapper.updateCoupleStatus(coupleInfo);
                userService.updateCoupleId(coupleInfo.getManId(), null);
                userService.updateCoupleId(coupleInfo.getWomanId(), null);
                return ResultStatus.SUCCESS;
            default:
                return ResultStatus.FAIL;
        }

    }
}
