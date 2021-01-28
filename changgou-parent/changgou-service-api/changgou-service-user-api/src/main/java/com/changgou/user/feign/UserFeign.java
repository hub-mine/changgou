package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable(value = "id") String id);
    /**
     * 增加积分
     * @param point
     */
    @GetMapping("/addPoint/{point}")
    public Result addPoint(@PathVariable("point") Integer point);
}
