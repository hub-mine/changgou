package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieTools;
import com.changgou.oauth.util.CookieUtil;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.ttl}")
    private Integer ttl;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private Integer cookieMaxAge;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    /**
     * 验证登录
     * @param username
     * @param password
     */
    @PostMapping("/login")
    public Result login(String username,String password){
        AuthToken login = userService.login(username, password, clientId, clientSecret);
        //数据存入cookie，设置cookie的时长
        //CookieUtil.addCookie(response,cookieDomain,"/","Authorization",login.getAccessToken(),cookieMaxAge,false);
        CookieTools.setCookie(request,response,"Authorization",login.getAccessToken(),cookieMaxAge,true);
        CookieTools.setCookie(request,response,"cuname",username);
        return new Result(true, StatusCode.ACCESSERROR,"登录成功");
    }
}
