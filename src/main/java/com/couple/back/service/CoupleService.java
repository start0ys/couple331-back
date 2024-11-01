package com.couple.back.service;

import com.couple.back.dto.CoupleStatusResponse;

public interface CoupleService {
    public CoupleStatusResponse getStatusType(Long userId) throws Exception;
}
