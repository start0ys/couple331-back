package com.couple.back.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.couple.back.common.CommonConstants.DateType;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.common.CommonUtil;
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
        String daysTogether = "";

        switch (statusType) {
            case REQUEST:
                message = "[ " + other + " ] " + messageType + " 커플 신청을 요청하였습니다.";
                break;
            case REJECT:
                message = "[ " + other + " ] " + messageType + " 커플 신청을 거절하였습니다.";
                break;
            case APPROVAL: 
                message = "[ " + other + " ] " + messageType + " 커플 신청을 승인하였습니다.";
                daysTogether = CommonUtil.calculateDaysFromToday(detail.getStartDate(), true, DateType.DAYS);
                break;
            case BREAKUP: 
                message = "[ " + other + " ] " + messageType + " 이별을 요청하였습니다.";
                break;
            case TERMINATED: 
                message = "[ " + other + " ] " + messageType + " 이별을 승인하였습니다.";
                break;
            case CONFIRMED:
                daysTogether = CommonUtil.calculateDaysFromToday(detail.getStartDate(), true, DateType.DAYS);
                break;
            default:
                return null;
        }


        return new CoupleStatusResponse(statusType, senderYn, message, coupleId, daysTogether);
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

        userService.updateCoupleId(couple.getManId(), coupleId, null);
        userService.updateCoupleId(couple.getWomanId(), coupleId, null);

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

        Long updateUserId = coupleStatusRequest.getUpdateUserId();
        boolean isSender = coupleInfo.getUpdateUserId() == updateUserId;

        coupleInfo.setUpdateUserId(updateUserId);


        switch (statusType) {
            case REQUEST:
                coupleInfo.setStatus(approvalType == ApprovalStatusType.APPROVE ? "03" : "02");
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            case REJECT:
                coupleMapper.deleteCoupleByCoupleId(coupleId);
                userService.updateCoupleId(coupleInfo.getManId(), null, coupleId);
                userService.updateCoupleId(coupleInfo.getWomanId(), null, coupleId);
                return ResultStatus.SUCCESS;
            case APPROVAL: 
                coupleInfo.setStatus(isSender ? "04" : "06"); // isSender면 승인한 사람이고 승인한 사람은 이별을 요청할수있고, isSender가 아니면 신청한 사람이므로 신청한사람이 확인하면 CONFIRMED로 상태 변경
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            case BREAKUP: 
                coupleInfo.setStatus("05");
                coupleMapper.updateCoupleStatus(coupleInfo);
                userService.updateCoupleId(coupleInfo.getManId(), null, coupleId);
                userService.updateCoupleId(coupleInfo.getWomanId(), null, coupleId);
                return ResultStatus.SUCCESS;
            case TERMINATED: // 사용자가 이별을 선언한 상태 => 상대방이 확인 하면 종료상태로 변경됨
                coupleInfo.setStatus("03");
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            case CONFIRMED: 
                coupleInfo.setStatus("04");
                coupleMapper.updateCoupleStatus(coupleInfo);
                return ResultStatus.SUCCESS;
            default:
                return ResultStatus.FAIL;
        }

    }

    public CoupleStatusDetail getCoupleDetail(Long coupleId) throws Exception {
        if(coupleId == null)
            throw new IllegalArgumentException("Parameter is Empty");

        CoupleStatusDetail detail = coupleMapper.selectCoupleStatusDetailByCoupleId(coupleId);
        if(detail == null)
            return null;

        CoupleStatusType statusType =  StatusConverter.getCoupleStatusType(detail.getStatus());


        return statusType == CoupleStatusType.APPROVAL || statusType == CoupleStatusType.CONFIRMED ? detail : null;
    }

    public ResultStatus updateCoupleDesc(Long coupleId, CoupleStatusRequest coupleStatusRequest) throws Exception {
        if(coupleId == null || coupleStatusRequest == null || coupleStatusRequest.getUpdateUserId() == null)
            throw new IllegalArgumentException("Parameter is Empty");

        Couple couple = new Couple();
        couple.setCoupleId(coupleId);
        couple.setUpdateUserId(coupleStatusRequest.getUpdateUserId());
        couple.setCoupleDesc(coupleStatusRequest.getCoupleDesc());

        coupleMapper.updateCoupleDesc(couple);

        return ResultStatus.SUCCESS;
    }
}
