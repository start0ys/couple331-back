package com.couple.back.service;

import com.couple.back.model.User;

public interface UserService {
    public User registerUser(User saveData) throws Exception;
}
