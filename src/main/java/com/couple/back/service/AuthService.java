package com.couple.back.service;

import com.couple.back.common.ApiResponse;
import com.couple.back.model.MailAuthRequest;
import com.couple.back.model.User;

public interface AuthService {
    public ApiResponse<String> sendCode(MailAuthRequest mailAuthRequest) throws Exception;
    public ApiResponse<String> verifyCode(MailAuthRequest mailAuthRequest) throws Exception;
    public ApiResponse<String> loginUser(User loginData) throws Exception;
    public boolean verifiedEmailCheck(String email) throws Exception;
}
