package com.changgou.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.util.IdWorker;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SeckillOrderListener {
    @Autowired
   private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @RabbitListener(queues = "seckill_queue")
    public void doSeckillOrder(String msg){
        SeckillStatus seckillStatus = JSON.parseObject(msg, SeckillStatus.class);
        String username = seckillStatus.getUsername();
        String time = seckillStatus.getTime();
        String goodsId = seckillStatus.getGoodsId();
        //判断缓存中是否已经存在客户订单
        Object order = stringRedisTemplate.boundHashOps("SeckillOrder").get(username);
        if(order!=null){
            seckillStatus.setStatus(3);
            stringRedisTemplate.boundValueOps("SeckillStatus_" + username+goodsId).set(JSONObject.toJSONString(seckillStatus));
            throw new RuntimeException("存在未支付订单");
        }
        //从商品队列获取数据，为空的话即没有库存
        String s = stringRedisTemplate.boundListOps("SeckillGoodsQueue_" + goodsId).rightPop();
        if(s==null){
            seckillStatus.setStatus(3);
            stringRedisTemplate.boundValueOps("SeckillStatus_" + username+goodsId).set(JSONObject.toJSONString(seckillStatus));
            throw new RuntimeException("商品卖完");
        }
        SeckillGoods seckillGoods = (SeckillGoods)redisTemplate.boundHashOps("SeckillGoods_" + time).get(goodsId);
        //下单
            SeckillOrder seckillOrder = new SeckillOrder();
            String orderId=idWorker.nextId()+"";
            seckillOrder.setId(orderId);
            seckillOrder.setSeckillId(goodsId);
            seckillOrder.setMoney(seckillGoods.getPrice().toString());
            seckillOrder.setUserId(username);
            seckillOrder.setCreateTime(seckillStatus.getCreateTime());
            seckillOrder.setStatus("0");
            //更新redis中的订单数据
            stringRedisTemplate.boundHashOps("SeckillOrder"+orderId).put(username,JSONObject.toJSONString(seckillOrder));
            //更新redis中的库存数据  ----存在超卖问题
//            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
            Long seckillStockCount = stringRedisTemplate.boundHashOps("SeckillStockCount").increment(goodsId, -1);
            seckillGoods.setStockCount(seckillStockCount.intValue());
            redisTemplate.boundHashOps("SeckillGoods_" + time).put(goodsId,seckillGoods);
            //排队状态更新
            seckillStatus.setStatus(2);
            seckillStatus.setOrderId(seckillOrder.getId());
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
            //返送消息到延迟队列中
        rabbitTemplate.convertAndSend("delayOrder_queue", (Object) JSONObject.toJSONString(seckillOrder), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration("300000");
                return message;
            }
        });
            stringRedisTemplate.boundValueOps("SeckillStatus_" + username+goodsId).set(JSONObject.toJSONString(seckillStatus));
            //判断商品已经卖完
            if(seckillStockCount==0){
                redisTemplate.boundHashOps("SeckillGoods_" + time).delete(goodsId);
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
            }
        }

    }

