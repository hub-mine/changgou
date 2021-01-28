package com.changgou.goods.feign;

import com.changgou.goods.pojo.Goods;
import com.changgou.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "goods")
@RequestMapping("/spu")
public interface GoodsFeign {
    /**
     * 根据id查询列表sku和spu
     */
    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable(value = "id") String id);
    @GetMapping("/goods/findAll")
    public Result<List<Goods>> findAllGoods();
}
