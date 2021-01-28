package com.changgou.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.util.IdWorker;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
    @RabbitListener(queues = "seckill_queue")
    public void doSeckillOrder(String msg){
        SeckillStatus seckillStatus = JSON.parseObject(msg, SeckillStatus.class);
        String username = seckillStatus.getUsername();
        String time = seckillStatus.getTime();
        String goodsId = seckillStatus.getGoodsId();
        //判断缓存中是否已经存在客户订单
        Object order = stringRedisTemplate.boundHashOps("SeckillOrder"+goodsId).get(username);
        if(order!=null){
            seckillStatus.setStatus(3);
            stringRedisTemplate.boundValueOps("SeckillStatus_" + username+goodsId).set(JSONObject.toJSONString(seckillStatus));
            throw new RuntimeException("存在未支付订单");
        }
        Object o = redisTemplate.boundHashOps("SeckillGoods_" + time).get(goodsId);
        if(o==null){
            seckillStatus.setStatus(3);
            stringRedisTemplate.boundValueOps("SeckillStatus_" + username+goodsId).set(JSONObject.toJSONString(seckillStatus));
            throw new RuntimeException("商品卖完");
        }
        SeckillGoods seckillGoods=(SeckillGoods)o;
        if(seckillGoods!=null&&seckillGoods.getStockCount()>0){
            //下单
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId()+"");
            seckillOrder.setSeckillId(goodsId);
            seckillOrder.setMoney(seckillGoods.getPrice().toString());
            seckillOrder.setUserId(username);
            seckillOrder.setCreateTime(seckillStatus.getCreateTime());
            seckillOrder.setStatus("0");
            //更新redis中的订单数据
            stringRedisTemplate.boundHashOps("SeckillOrder"+goodsId).put(username,JSONObject.toJSONString(seckillOrder));
            //更新redis中的库存数据
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
            redisTemplate.boundHashOps("SeckillGoods_" + time).put(goodsId,seckillGoods);
            //排队状态更新
            seckillStatus.setStatus(2);
            seckillStatus.setOrderId(seckillOrder.getId());
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));
            stringRedisTemplate.boundValueOps("SeckillStatus_" + username+goodsId).set(JSONObject.toJSONString(seckillStatus));
        }
        //判断商品已经卖完
        if(seckillGoods.getStockCount()<+0){
            redisTemplate.boundHashOps("SeckillGoods_" + time).delete(goodsId);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
        }
    }
}
