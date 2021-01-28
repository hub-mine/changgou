package com.changgou.pay.feign;

import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "pay")
public interface PayFeign {
    @RequestMapping("/check")
    public Result check(String orderId);
}
