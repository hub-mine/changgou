package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

public interface CartService {
    /**
     * 新增购物车
     * @param skuId
     * @param num
     */
    void add(String skuId,Integer num,String username);

    /**
     * 查询购物车列表
     * @param username
     * @return
     */
    List<OrderItem> list(String username);
    /**
     * 查询购物车列表
     * @param username
     * @return
     */
    List<OrderItem> list(String username,String[] ids);
}
