package com.changgou.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import com.changgou.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PayListener {
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @RabbitListener(queues = "order_queue")
    public void payListener(String msg){
        Map<String,String> map = JSONObject.parseObject(msg, Map.class);
        String return_code = map.get("return_code");
        if(return_code.equalsIgnoreCase("SUCCESS")){
            String out_trade_no = map.get("out_trade_no");
            String result_code = map.get("result_code");
            if(result_code.equalsIgnoreCase("SUCCESS")){
                String transaction_id = map.get("transaction_id");
                //调用修改订单方法---------支付成功
                orderService.updateOrder(out_trade_no,transaction_id);
            }else {
                //调用删除订单方法------支付失败
                orderService.deleteOrder(out_trade_no);
            }

        }
    }
    @RabbitListener(queues = "normal_queue")  //监听消费延迟队列消息，若未支付则删除订单
    public void orderListener(String msg){
        try {
            Order order = JSONObject.parseObject(msg, Order.class);
            //调用微信接口，关闭订单
            String url="https://api.mch.weixin.qq.com/pay/closeorder";
            HashMap<String, String> map = new HashMap<>();
            map.put("appid", appid);
            map.put("mch_id", partner);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("out_trade_no", order.getId());
            String xml = WXPayUtil.generateSignedXml(map, partnerkey);
            //发起请求,使用httpclient工具请求
            HttpClient httpClient = new HttpClient(url);
            httpClient.setXmlParam(xml);
            httpClient.setHttps(true);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            //获取请求结果
            String return_code = resultMap.get("return_code");
            if(return_code.equalsIgnoreCase("SUCCESS")){
                String result_code = resultMap.get("result_code");
                if(result_code.equalsIgnoreCase("SUCCESS")){
                    //查询订单数据库支付状态
                    Order order1 = orderMapper.selectByPrimaryKey(order.getId());
                    if(order1.getPayStatus().equalsIgnoreCase("0")){
                        orderService.deleteOrder(order.getId());
                        //更新redis
                        redisTemplate.boundHashOps("OrderInfo_").delete(order.getId());
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }
}
