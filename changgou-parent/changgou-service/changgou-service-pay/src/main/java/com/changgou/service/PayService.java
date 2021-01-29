package com.changgou.service;

import java.util.Map;

public interface PayService {
    /**
     * 生成二维码连接
<<<<<<< HEAD
     * @param data

     * @return
     */
    String createCode(Map<String,Object>data);
=======
     * @param orderId
     * @param money
     * @return
     */
    String createCode(String orderId,Integer money);
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e

    /**
     * 从微信查询订单信息
     * @param orderId
     * @return
     */
    Map<String,String> checkOrder(String orderId);
}
