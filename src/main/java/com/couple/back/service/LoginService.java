package com.couple.back.service;

import com.couple.back.common.ApiResponse;
import com.couple.back.model.User;

public interface LoginService {
    public ApiResponse<String> loginUser(User loginData) throws Exception;
}
