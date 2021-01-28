package com.changgou.oauth.service.impl;


import com.changgou.oauth.service.UserService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    /**
     * 认证登录服务
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //定义生成令牌的url
        String s = loadBalancerClient.choose("user-auth").getUri().toString();
        String url=s+"/oauth/token";
        //拼接请求参数
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //拼接body
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        //拼接header
        String headerParam =getHeader(clientId,clientSecret);
        if(headerParam==null){
            throw new RuntimeException("请求头异常");
        }
        header.add("Authorization",headerParam);
        //调用restTemplate请求url
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, header), Map.class);
        //获取请求数据
        Map<String,String> result = exchange.getBody();
        //封装数据
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(result.get("access_token"));
        authToken.setRefreshToken(result.get("refresh_token"));
        authToken.setJti("jti");
        return authToken;
    }

    /**
     * 拼接header
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String getHeader(String clientId, String clientSecret) {
        try {
            String param = clientId+":"+clientSecret;
            byte[] encode = Base64.getEncoder().encode(param.getBytes("UTF-8"));
            return "Basic "+new String(encode,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
