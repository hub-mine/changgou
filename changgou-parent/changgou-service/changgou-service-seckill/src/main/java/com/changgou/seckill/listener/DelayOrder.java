package com.changgou.seckill.listener;

import com.alibaba.fastjson.JSONObject;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
@Component
public class DelayOrder {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @RabbitListener(queues = "normalOrder_queue")
    public void listen2Delay(String msg){
        SeckillOrder seckillOrder = JSONObject.parseObject(msg, SeckillOrder.class);
        String id = seckillOrder.getId();
        String userId = seckillOrder.getUserId();
        SeckillOrder seckillOrder1 = seckillOrderMapper.selectByPrimaryKey(id);
        if(seckillOrder1==null|| StringUtils.isEmpty(id)){
            Object o = stringRedisTemplate.boundHashOps("SeckillOrder" + id).get(userId);
            SeckillOrder seckillOrder2 = JSONObject.parseObject(o.toString(), SeckillOrder.class);
            //支付成功的话调用更新订单状态，更新数据到数据库中
            if (seckillOrder2.getStatus().equalsIgnoreCase("2")){
                String transactionId = seckillOrder2.getTransactionId();
                seckillOrderService.updateSeckillOrder(userId,id,transactionId);
                return;
            }else {
                String s = stringRedisTemplate.boundValueOps("SeckillStatus_" + seckillOrder.getUserId()).get();
                SeckillStatus seckillStatus = JSONObject.parseObject(s, SeckillStatus.class);
                //插入超时订单数据到数据库中
                seckillOrder2.setStatus("3");
                seckillOrderMapper.insertSelective(seckillOrder2);
                //调用回滚方法回滚商品库存数据
                String seckillId = seckillOrder2.getSeckillId();
                String time = seckillStatus.getTime();
                seckillOrderService.rollbackSeckillGoodsStockNum(seckillId,time);
            }
        }
    }
}
