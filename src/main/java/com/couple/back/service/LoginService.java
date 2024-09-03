package com.couple.back.service;

import java.util.Map;

import com.couple.back.model.User;

public interface LoginService {
    public Map<String, Object> loginUser(User loginData) throws Exception;
}
