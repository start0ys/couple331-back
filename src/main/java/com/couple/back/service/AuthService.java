package com.couple.back.service;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.dto.JwtTokenResponse;
import com.couple.back.dto.LoginRequest;
import com.couple.back.dto.MailAuthRequest;

public interface AuthService {
    public ApiResponse<String> sendCode(MailAuthRequest mailAuthRequest) throws Exception;
    public ApiResponse<String> verifyCode(MailAuthRequest mailAuthRequest) throws Exception;
    public ApiResponse<JwtTokenResponse> loginUser(LoginRequest loginData) throws Exception;
    public ApiResponse<JwtTokenResponse> refreshAccessToken(String authorizationHeader) throws Exception;
    public ResultStatus validateToken(String authorizationHeader) throws Exception;
    public void logout(String authorizationHeader) throws Exception;
    public boolean verifiedEmailCheck(String email) throws Exception;
}
