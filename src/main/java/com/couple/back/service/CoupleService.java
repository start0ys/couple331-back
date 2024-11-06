package com.couple.back.service;

import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.dto.CoupleStatusRequest;
import com.couple.back.dto.CoupleStatusResponse;
import com.couple.back.model.Couple;

public interface CoupleService {
    public Couple registerCouple(Couple couple) throws Exception;
    public CoupleStatusResponse getStatusType(Long userId) throws Exception;
    public ResultStatus updateCoupleStatus(Long coupleId, CoupleStatusRequest coupleStatusRequest) throws Exception;
}
