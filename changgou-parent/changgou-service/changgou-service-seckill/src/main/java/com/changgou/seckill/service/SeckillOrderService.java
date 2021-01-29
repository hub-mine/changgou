package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:SeckillOrder业务层接口
 *****/
public interface SeckillOrderService {
    /**
<<<<<<< HEAD
     * 定义支付失败以及订单超时的库存回滚方法
     * @param orderId
     * @param time
     */
    void rollbackSeckillGoodsStockNum(String orderId, String time);
    /**
     * 通过监听支付结果进行订单修改
     * @param username
     * @param orderId
     * @param transaction_id
     */
    void updateSeckillOrder(String username,String orderId,String transaction_id);

    /**
     * 通过监听支付结果进行订单删除
     * @param username
     * @param orderId
     */
    void deleteSeckillOrder(String username,String orderId);
    /**
=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
     * 秒杀下单
     * @param userName
     * @param id
     * @param time
     * @return
     */
    SeckillStatus add(String userName, String id, String time);
    /***
     * SeckillOrder多条件分页查询
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    /***
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(int page, int size);

    /***
     * SeckillOrder多条件搜索方法
     * @param seckillOrder
     * @return
     */
    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    /***
     * 删除SeckillOrder
     * @param id
     */
    void delete(String id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     */
    void update(SeckillOrder seckillOrder);

    /***
     * 新增SeckillOrder
     * @param seckillOrder
     */
    void add(SeckillOrder seckillOrder);

    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
     SeckillOrder findById(String id);

    /***
     * 查询所有SeckillOrder
     * @return
     */
    List<SeckillOrder> findAll();
}
