package com.changgou.goods.dao;
import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:itheima
 * @Description:Sku的Dao
 *****/
public interface SkuMapper extends Mapper<Sku> {
    /**
     * 扣除库存
     * 考虑超卖问题，防止出现负库存
     */
    @Update("update tb_sku set num=num-#{num},sale_num=sale_num+#{num} where num>=#{num} and id=#{id}")
    int deCount(@Param(value = "id") String id ,@Param("num") Integer num);

    /**
     * 关联订单回滚库存
     * @param id
     * @param num
     * @return
     */
    @Update("update tb_sku set num=num+#{num},sale_num=sale_num-#{num} where id=#{id}")
    int rollBack(@Param(value = "id") String id ,@Param("num") Integer num);
}
