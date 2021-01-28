package com.changgou.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private RedisTemplate redisTemplate;   //这个类型redis的key会有hash前缀
    @Autowired
    private StringRedisTemplate stringRedisTemplate;        //存入的key是什么就是什么
    /**
     * 新增购物车
     * @param skuId
     * @param num
     */
    @Override
    public void add(String skuId, Integer num,String username) {
        if(num<=0){    //删除购物车商品
            redisTemplate.boundHashOps("Cart_"+username).delete();
            return;
        }
        //调用方法封装数据
        OrderItem orderItem = getOrderItem(skuId, num);
        //将数据存入redis
        redisTemplate.boundHashOps("Cart_"+username).put(skuId,orderItem);  //带前缀
        //数据处理
//        String s = JSONObject.toJSONString(orderItem);
//        stringRedisTemplate.boundHashOps("Cart_"+username).put(skuId,s);  //不带前缀存储类型Map(String,Map(String,String))
    }

    /**
     * 查询购物车列表
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> list = redisTemplate.boundHashOps("Cart_" + username).values();
        return list;
    }
    /**
     * 查询购物车列表
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username, String[] ids) {
        List<OrderItem> list = new ArrayList<>();
        for (String id : ids) {
            OrderItem orderItem = (OrderItem)redisTemplate.boundHashOps("Cart_" + username).get(id);
            list.add(orderItem);
        }
        return list;
    }

    /**
     * 代码优化
     */
    private OrderItem getOrderItem(String skuId, Integer num){
        Sku sku = skuFeign.findById(skuId).getData();
        if(sku==null|| StringUtils.isEmpty(sku.getId())){
            throw new RuntimeException("商品不存在");
        }
        Spu spu = spuFeign.findById(sku.getSpuId()).getData();
        if(spu==null||StringUtils.isEmpty(spu.getId())){
            throw new RuntimeException("商品不存在");
        }
        OrderItem orderItem = new OrderItem();
        //数据封装
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId());
        orderItem.setSkuId(skuId);
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney((sku.getPrice())*num);
        orderItem.setImage(sku.getImage());
        orderItem.setIsReturn("0");
        return orderItem;
    }
}
