package com.couple.back.common;

import org.apache.commons.lang3.StringUtils;

import com.couple.back.model.Couple;
import com.couple.back.model.User;

public class StatusConverter {
    public static enum GenderType {MAN, WOMAN};
    public static enum CoupleStatusType {REQUEST, REJECT, APPROVAL, CONFIRMED, BREAKUP, TERMINATED};
    public static enum ApprovalStatusType  {APPROVE, REJECT};

    public static GenderType getGenderType(User user) {
        if(user == null) 
            return null;

        return getGenderType(user.getGender());
    }

    public static GenderType getGenderType(String gender) {
        if(StringUtils.isEmpty(gender)) 
            return null;

        switch (gender) {
            case "01":
                return GenderType.MAN;
            case "02":
                return GenderType.WOMAN;
            default:
                return null;
        }
    }

    public static CoupleStatusType getCoupleStatusType(String status) {
        if(StringUtils.isEmpty(status))
            return null;

        switch (status) {
            case "01": // 처음 신청한 상태
                return CoupleStatusType.REQUEST;
            case "02": // 상대방이 거절한 상태
                return CoupleStatusType.REJECT;
            case "03": // 수락하여 정식 커플이 된 상태
                return CoupleStatusType.APPROVAL;
            case "04": // 사용자가 이별을 선언한 상태 => 상대방이 확인 하면 종료상태로 변경됨
                return CoupleStatusType.BREAKUP;
            case "05": // 종료상태  => 추후 다시 동일 계정끼리 신청이되어 수락되면 다시 상태가 03로 변경
                return CoupleStatusType.TERMINATED;
            case "06": // 수락하여 정식 커플이 된 상태를 처음 신청한 사람이 확인한 상태
                return CoupleStatusType.CONFIRMED;
            default:
                return null;
        }
    }

    public static ApprovalStatusType getApprovalStatusType(String approvalStatusType) {
        if(StringUtils.isEmpty(approvalStatusType))
            return null;

        switch (approvalStatusType) {
            case "01": // 처음 신청한 상태
                return ApprovalStatusType.APPROVE;
            case "02": // 상대방이 거절한 상태
                return ApprovalStatusType.REJECT;
            default:
                return null;
        }
    }
}
