package com.changgou.oauth.intercepter;

import com.changgou.oauth.util.JwtToken;
import com.changgou.util.JwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * 发生远程的feign调用之前,会出发这个方法,然后再进行远程调用
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        //生成临时令牌
        String token = JwtToken.adminJwt();
        //将临时令牌放入请求头中去
        template.header("Authorization", "bearer " + token);
        //使用RequestContextHolder工具获取request相关变量
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            //取出request
            HttpServletRequest request = attributes.getRequest();
            //获取所有头文件信息的key
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    //头文件的key
                    String name = headerNames.nextElement();
                    //头文件的value
                    String values = request.getHeader(name);
                    //将令牌数据添加到头文件中
                    template.header(name, values);
                }
            }
        }
    }
}
