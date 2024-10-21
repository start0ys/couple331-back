package com.couple.back.service;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.model.MailAuthRequest;
import com.couple.back.model.MailAuthRequest.AuthCodeStatus;
import com.couple.back.model.User;

public interface AuthService {
    public AuthCodeStatus sendCode(MailAuthRequest mailAuthRequest) throws Exception;
    public ResultStatus verifyCode(MailAuthRequest mailAuthRequest) throws Exception;
    public ApiResponse<String> loginUser(User loginData) throws Exception;
}
