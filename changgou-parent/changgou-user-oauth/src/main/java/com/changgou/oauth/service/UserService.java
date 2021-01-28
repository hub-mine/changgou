package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

public interface UserService {
    /**
     * 认证服务
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    AuthToken  login(String username,String password,String clientId,String clientSecret);
}
