package com.changgou.content.feign;

import com.changgou.content.pojo.Content;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "content")
public interface ContentFeign {
    /**
     * 根据CategoryId查询content
     */
    @GetMapping("/content/category/{id}")
    public Result<List<Content>> findByCategoryId(@PathVariable(value = "id") Long id);
}
