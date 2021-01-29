package com.changgou.service;

import java.util.Map;

public interface PayService {
    /**
     * 生成二维码连接
     * @param data

     * @return
     */
    String createCode(Map<String,Object>data);

    /**
     * 从微信查询订单信息
     * @param orderId
     * @return
     */
    Map<String,String> checkOrder(String orderId);
}
