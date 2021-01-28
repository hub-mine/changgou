package com.changgou.search.feign;

import com.changgou.util.Result;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "search")
@RequestMapping("/search")
public interface SearchFeign {
    /**
     * 关键字查询
     */
    @GetMapping
    public Result<Map<String,Object>> search(@RequestParam Map<String,String>data);
}
