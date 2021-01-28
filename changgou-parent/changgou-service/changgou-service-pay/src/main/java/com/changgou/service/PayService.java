package com.changgou.service;

import java.util.Map;

public interface PayService {
    /**
     * 生成二维码连接
     * @param orderId
     * @param money
     * @return
     */
    String createCode(String orderId,Integer money);

    /**
     * 从微信查询订单信息
     * @param orderId
     * @return
     */
    Map<String,String> checkOrder(String orderId);
}
