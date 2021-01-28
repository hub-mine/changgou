package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Brand的Dao
 *****/
public interface BrandMapper extends Mapper<Brand> {
    @Select("SELECT tb.* FROM `tb_category_brand` tcb ,tb_brand  tb where tcb.category_id=4 and tcb.brand_id=tb.id"
    )
    List<Brand> findByCategoryId(Integer id);
}
