package com.changgou.filter;

import com.changgou.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "Authorization";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //获取url
        String path = request.getURI().getPath();
        if(path.startsWith("/api/user/login")){
            return chain.filter(exchange);
        }
        //获取令牌信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        if(token==null){
            token=request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }
        //从cookie中获取令牌
        if(StringUtils.isEmpty(token)){
           HttpCookie first = request.getCookies().getFirst(AUTHORIZE_TOKEN);
           if(first==null){
               response.setStatusCode(HttpStatus.UNAUTHORIZED);
               //这个方法结束请求
               return response.setComplete();
           }
           //cookie的令牌数据
            token = first.getValue();
        }
        if(StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //这个方法结束请求
            return response.setComplete();
        }
        //将cookie放入header中
        request.mutate().header(AUTHORIZE_TOKEN,"bearer "+token);
        //放行
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return 0;
    }
}
