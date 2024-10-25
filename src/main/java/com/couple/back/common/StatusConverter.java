package com.couple.back.common;

import org.apache.commons.lang3.StringUtils;

import com.couple.back.model.Couple;
import com.couple.back.model.User;

public class StatusConverter {
    public static enum GenderType {MAN, WOMAN};
    public enum CoupleStatusType {PENDING, ACCEPTED, REJECTED, CONFIRMED, BREAKUP, TERMINATED};

    public GenderType getGenderType(User user) {
        if(user == null || StringUtils.isEmpty(user.getGender())) 
            return null;

        switch (user.getGender()) {
            case "01":
                return GenderType.MAN;
            case "02":
                return GenderType.WOMAN;
            default:
                return null;
        }
    }

    public CoupleStatusType getCoupleStatusType(Couple couple) {
        if(couple == null || StringUtils.isEmpty(couple.getStatus()))
            return null;

        switch (couple.getStatus()) {
            case "01": // 처음 신청한 상태
                return CoupleStatusType.PENDING;
            case "02": // 상대방이 수락한 상태 => 신청자가 확인하면 정상 상태로 변경됨
                return CoupleStatusType.ACCEPTED; 
            case "03": // 상대방이 거절한 상태 => 신청자가 확인하면 데이터 제거됨
                return CoupleStatusType.REJECTED;
            case "04": // 수락 후 확인을 거쳐 정식 커플이 된 상태
                return CoupleStatusType.CONFIRMED;
            case "05": // 사용자가 이별을 선언한 상태 => 상대방이 확인 하면 종료상태로 변경됨
                return CoupleStatusType.BREAKUP;
            case "06": // 종료상태  => 추후 다시 동일 계정끼리 신청이되어 수락되면 다시 상태가 04로 변경
                return CoupleStatusType.TERMINATED;
            default:
                return null;
        }
    }
}
