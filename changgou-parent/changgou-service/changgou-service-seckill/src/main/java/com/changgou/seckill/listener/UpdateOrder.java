package com.changgou.seckill.listener;

import com.alibaba.fastjson.JSONObject;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.service.SeckillOrderService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateOrder {
    @Autowired
    private SeckillOrderService seckillOrderService;
    @RabbitListener(queues = "seckillOrder_queue")
    public void listen2Pay(String msg) {
        Map<String,String> result = JSONObject.parseObject(msg, Map.class);
        String return_code = result.get("return_code");
        if(return_code.equalsIgnoreCase("SUCCESS")){
            String result_code = result.get("result_code");
            String attach = result.get("attach");
            Map map = JSONObject.parseObject(attach, Map.class);
            String username = map.get("username").toString();
            //订单号
            String out_trade_no = result.get("out_trade_no");
            //流水号
            String transaction_id = result.get("transaction_id");
            if(result_code.equalsIgnoreCase("SUCCESS")){
                seckillOrderService.updateSeckillOrder(username,out_trade_no,transaction_id);
            }else{
                seckillOrderService.deleteSeckillOrder(username,out_trade_no);
            }
        }
    }
}
