package com.changgou.seckill.impl;

import com.alibaba.fastjson.JSONObject;
<<<<<<< HEAD
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
=======
import com.changgou.seckill.dao.SeckillOrderMapper;
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.util.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/****
 * @Author:itheima
 * @Description:SeckillOrder业务层接口实现类
 *****/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
<<<<<<< HEAD
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    /**
     * 通过监听支付结果进行订单修改
     * @param username
     * @param orderId
     * @param transaction_id
     */
    @Override
    public void updateSeckillOrder(String username, String orderId, String transaction_id) {
        String order = stringRedisTemplate.boundHashOps("SeckillOrder" + orderId).get(username).toString();
        SeckillOrder seckillOrder = JSONObject.parseObject(order, SeckillOrder.class);
        if(seckillOrder.getStatus().equalsIgnoreCase("0")){
            seckillOrder.setStatus("1");
            seckillOrder.setTransactionId(transaction_id);
            seckillOrder.setPayTime(new Date());
            //更新数据到数据库中
            seckillOrderMapper.updateByPrimaryKeySelective(seckillOrder);
            //更新redis中订单数据
            stringRedisTemplate.boundHashOps("SeckillOrder" + orderId).put(username,JSONObject.toJSONString(seckillOrder));
            //移除排队信息
            stringRedisTemplate.delete("SeckillStatus_" + username);
            stringRedisTemplate.delete("UserQueueCount_" + username);
        }
    }
    /**
     * 通过监听支付结果进行订单删除
     * @param username
     * @param orderId
     */
    @Override
    public void deleteSeckillOrder(String username, String orderId) {
        String order = stringRedisTemplate.boundHashOps("SeckillOrder" + orderId).get(username).toString();
        SeckillOrder seckillOrder = JSONObject.parseObject(order, SeckillOrder.class);
        String s = stringRedisTemplate.boundValueOps("SeckillStatus_" + seckillOrder.getUserId()).get();
        SeckillStatus seckillStatus = JSONObject.parseObject(s, SeckillStatus.class);
        if(seckillOrder.getStatus().equalsIgnoreCase("0")){
            seckillOrder.setStatus("2");
            //更新数据到数据库中
            seckillOrderMapper.updateByPrimaryKeySelective(seckillOrder);
            //更新redis中订单数据
            stringRedisTemplate.boundHashOps("SeckillOrder" + orderId).put(username,JSONObject.toJSONString(seckillOrder));
            //移除排队信息
            stringRedisTemplate.delete("SeckillStatus_" + username);
            stringRedisTemplate.delete("UserQueueCount_" + username);
        }
        rollbackSeckillGoodsStockNum(seckillOrder.getSeckillId(),seckillStatus.getTime());
    }

    /**
     * 定义支付失败以及订单超时的库存回滚方法
     * @param orderId
     * @param time
     */
    public void rollbackSeckillGoodsStockNum(String orderId, String time) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectByPrimaryKey(orderId);
        String seckillId = seckillOrder.getSeckillId();
        SeckillGoods seckillGoods = (SeckillGoods)redisTemplate.boundHashOps("SeckillGoods_" + time).get(seckillId);
        //redis中商品库存找到
        if(seckillGoods!=null&&StringUtils.isEmpty(seckillGoods.getId())){
            //redis库存加1
            stringRedisTemplate.boundListOps("SeckillGoodsQueue_"+seckillGoods.getId()).leftPush(seckillId);//商品队列放回数据
            Long seckillStockCount = stringRedisTemplate.boundHashOps("SeckillStockCount").increment(seckillId, 1);
            seckillGoods.setStockCount(seckillStockCount.intValue());
            redisTemplate.boundHashOps("SeckillGoods_" + time).put(seckillId,seckillGoods);
            //redis中商品售罄，直接更新数据库信息
        }else{
            SeckillGoods seckillGoods1 = seckillGoodsMapper.selectByPrimaryKey(seckillId);
            seckillGoods1.setStockCount(seckillGoods1.getStockCount()+1);
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods1);
        }


    }

=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
    /**
     * 秒杀下单
     * @param userName
     * @param id
     * @param time
     * @return
     */
    @Override
    public SeckillStatus add(String userName, String id, String time) {
        /**
         * 处理排队重复问题
         */
<<<<<<< HEAD
        Long increment = stringRedisTemplate.boundValueOps("UserQueueCount_" + userName).increment(1);
=======
        Long increment = redisTemplate.boundValueOps("UserQueueCount_" + userName).increment(1);
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
        if(increment>1){
            throw new RuntimeException("正在排队");
        }
        SeckillStatus seckillStatus = new SeckillStatus();
        seckillStatus.setGoodsId(id);
        seckillStatus.setUsername(userName);
        seckillStatus.setTime(time);
        seckillStatus.setStatus(1);
        //排队状态存入redis中
<<<<<<< HEAD
        stringRedisTemplate.boundValueOps("SeckillStatus_" + userName).set(JSONObject.toJSONString(seckillStatus));
=======
        stringRedisTemplate.boundValueOps("SeckillStatus_" + userName+id).set(JSONObject.toJSONString(seckillStatus));
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
        //发送队列实现下单，同时更新排队状态和减库存等操作
        rabbitTemplate.convertAndSend("seckill_exchange","seckill.goods", JSONObject.toJSONString(seckillStatus));
        return seckillStatus;
    }

    /**
     * SeckillOrder条件+分页查询
     * @param seckillOrder 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            // 主键
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                    criteria.andEqualTo("id",seckillOrder.getId());
            }
            // 秒杀商品ID
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                    criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            // 支付金额
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                    criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            // 用户
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                    criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            // 创建时间
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                    criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            // 支付时间
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                    criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                    criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            // 收货人
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                    criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            // 交易流水
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                    criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 增加SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void add(SeckillOrder seckillOrder){
        seckillOrderMapper.insert(seckillOrder);
    }

    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(String id){
        return  seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillOrder全部数据
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }
}
