package com.changgou.goods.feign;

import com.changgou.goods.pojo.Category;
import com.changgou.util.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "goods")
@RequestMapping("/category")
public interface CategoryFeign {
    /***
     * 根据ID查询Category数据
     * @param id
     * @return
     */
    @ApiOperation(value = "Category根据ID查询",notes = "根据ID查询Category方法详情",tags = {"CategoryController"})
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", required = true, dataType = "Integer")
    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable(value = "id") Integer id);
}
