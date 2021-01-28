package com.changgou.task;

import com.alibaba.fastjson.JSONObject;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.PayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class OrderTask {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PayFeign payFeign;
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "* 10 * * * *")
    public void task(){
        //查询数据库支付状态为0
        Order order = new Order();
        order.setPayStatus("0");
        List<Order> select = orderMapper.select(order);
        //遍历调用支付服务中的查询状态
        for (Order order1 : select) {
            String data = (String)payFeign.check(order1.getId()).getData();  //xml格式
            Map<String,String> map = JSONObject.parseObject(data, Map.class);
            //根据微信状态修改数据库
            if(map.get("trade_state").equalsIgnoreCase("SUCCESS")){  //支付成功
                order1.setPayTime(new Date());
                order1.setPayStatus("1");
                orderMapper.updateByPrimaryKeySelective(order1);
            }else if(map.get("trade_state").equalsIgnoreCase("PAYERROR")){  //支付失败
                order1.setIsDelete("1");
                orderMapper.updateByPrimaryKeySelective(order1);
                redisTemplate.boundHashOps("OrderInfo_").delete(order1.getId());
            }
        }
    }
}
