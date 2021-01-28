package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "goods")
public interface SkuFeign {
    @GetMapping("/sku/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable(value = "status") String status);
    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable(value = "id") String id);
    /**
     * 关联订单减库存
     */
    @GetMapping("/sku/deCount")
    public Result deCount(Map<String,Object> map);
    /**
     * 关联订单回滚库存
     */
    @GetMapping("/sku/rollBack")
    public Result rollBack(Map<String,Object>map);
}
