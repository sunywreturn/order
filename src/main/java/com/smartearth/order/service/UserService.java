package com.smartearth.order.service;

import com.smartearth.order.pojo.Response;

import java.util.Map;

public interface UserService {
    Response login(String username, String password);

    int register(int type,String jsonData, String code);

    void updatePassword(String username, String password);

    Map<String, Object> getUserByUsername(String username);

    int updateCompanyName(String userId, String companyName);

    Map<String, Object> getUserInfo(String userId);

    void auth(int type, String userJsonData, String userId);

    void updateUserAndAuth(String userJsonData, String authJsonData);

}
