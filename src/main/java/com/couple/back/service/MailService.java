package com.couple.back.service;

import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.model.MailRequest;

public interface MailService {
    public String sendVerificationCode(MailRequest mailRequest) throws Exception;
    public ResultStatus verifyCode(MailRequest mailRequest) throws Exception;
}
