package com.couple.back.service;

import com.couple.back.model.User;

import java.util.List;

public interface UserService {
    public User registerUser(User saveData) throws Exception;
    public User getUser(Long userId) throws Exception;
    public List<User> getOppositeGenderSingleUsers(String gender) throws Exception;
    public void updateCoupleId(Long userId, Long afterCoupleId, Long beforeCoupleId) throws Exception;
}
