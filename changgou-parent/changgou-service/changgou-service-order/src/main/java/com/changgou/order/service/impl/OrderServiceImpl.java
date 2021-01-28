package com.changgou.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.pojo.OrderLog;
import com.changgou.order.service.OrderLogService;
import com.changgou.order.service.OrderService;
import com.changgou.user.feign.UserFeign;
import com.changgou.util.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * @Author:itheima
 * @Description:Order业务层接口实现类
 *****/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderLogService orderLogService;
    /**
     * Order条件+分页查询
     * @param order 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Order> findPage(Order order, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(order);
        //执行搜索
        return new PageInfo<Order>(orderMapper.selectByExample(example));
    }

    /**
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Order> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Order>(orderMapper.selectAll());
    }

    /**
     * Order条件查询
     * @param order
     * @return
     */
    @Override
    public List<Order> findList(Order order){
        //构建查询条件
        Example example = createExample(order);
        //根据构建的条件查询数据
        return orderMapper.selectByExample(example);
    }


    /**
     * Order构建查询对象
     * @param order
     * @return
     */
    public Example createExample(Order order){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(order!=null){
            // 订单id
            if(!StringUtils.isEmpty(order.getId())){
                    criteria.andEqualTo("id",order.getId());
            }
            // 数量合计
            if(!StringUtils.isEmpty(order.getTotalNum())){
                    criteria.andEqualTo("totalNum",order.getTotalNum());
            }
            // 金额合计
            if(!StringUtils.isEmpty(order.getTotalMoney())){
                    criteria.andEqualTo("totalMoney",order.getTotalMoney());
            }
            // 优惠金额
            if(!StringUtils.isEmpty(order.getPreMoney())){
                    criteria.andEqualTo("preMoney",order.getPreMoney());
            }
            // 邮费
            if(!StringUtils.isEmpty(order.getPostFee())){
                    criteria.andEqualTo("postFee",order.getPostFee());
            }
            // 实付金额
            if(!StringUtils.isEmpty(order.getPayMoney())){
                    criteria.andEqualTo("payMoney",order.getPayMoney());
            }
            // 支付类型，1、在线支付、0 货到付款
            if(!StringUtils.isEmpty(order.getPayType())){
                    criteria.andEqualTo("payType",order.getPayType());
            }
            // 订单创建时间
            if(!StringUtils.isEmpty(order.getCreateTime())){
                    criteria.andEqualTo("createTime",order.getCreateTime());
            }
            // 订单更新时间
            if(!StringUtils.isEmpty(order.getUpdateTime())){
                    criteria.andEqualTo("updateTime",order.getUpdateTime());
            }
            // 付款时间
            if(!StringUtils.isEmpty(order.getPayTime())){
                    criteria.andEqualTo("payTime",order.getPayTime());
            }
            // 发货时间
            if(!StringUtils.isEmpty(order.getConsignTime())){
                    criteria.andEqualTo("consignTime",order.getConsignTime());
            }
            // 交易完成时间
            if(!StringUtils.isEmpty(order.getEndTime())){
                    criteria.andEqualTo("endTime",order.getEndTime());
            }
            // 交易关闭时间
            if(!StringUtils.isEmpty(order.getCloseTime())){
                    criteria.andEqualTo("closeTime",order.getCloseTime());
            }
            // 物流名称
            if(!StringUtils.isEmpty(order.getShippingName())){
                    criteria.andEqualTo("shippingName",order.getShippingName());
            }
            // 物流单号
            if(!StringUtils.isEmpty(order.getShippingCode())){
                    criteria.andEqualTo("shippingCode",order.getShippingCode());
            }
            // 用户名称
            if(!StringUtils.isEmpty(order.getUsername())){
                    criteria.andLike("username","%"+order.getUsername()+"%");
            }
            // 买家留言
            if(!StringUtils.isEmpty(order.getBuyerMessage())){
                    criteria.andEqualTo("buyerMessage",order.getBuyerMessage());
            }
            // 是否评价
            if(!StringUtils.isEmpty(order.getBuyerRate())){
                    criteria.andEqualTo("buyerRate",order.getBuyerRate());
            }
            // 收货人
            if(!StringUtils.isEmpty(order.getReceiverContact())){
                    criteria.andEqualTo("receiverContact",order.getReceiverContact());
            }
            // 收货人手机
            if(!StringUtils.isEmpty(order.getReceiverMobile())){
                    criteria.andEqualTo("receiverMobile",order.getReceiverMobile());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(order.getReceiverAddress())){
                    criteria.andEqualTo("receiverAddress",order.getReceiverAddress());
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(!StringUtils.isEmpty(order.getSourceType())){
                    criteria.andEqualTo("sourceType",order.getSourceType());
            }
            // 交易流水号
            if(!StringUtils.isEmpty(order.getTransactionId())){
                    criteria.andEqualTo("transactionId",order.getTransactionId());
            }
            // 订单状态,0:未完成,1:已完成，2：已退货
            if(!StringUtils.isEmpty(order.getOrderStatus())){
                    criteria.andEqualTo("orderStatus",order.getOrderStatus());
            }
            // 支付状态,0:未支付，1：已支付，2：支付失败
            if(!StringUtils.isEmpty(order.getPayStatus())){
                    criteria.andEqualTo("payStatus",order.getPayStatus());
            }
            // 发货状态,0:未发货，1：已发货，2：已收货
            if(!StringUtils.isEmpty(order.getConsignStatus())){
                    criteria.andEqualTo("consignStatus",order.getConsignStatus());
            }
            // 是否删除
            if(!StringUtils.isEmpty(order.getIsDelete())){
                    criteria.andEqualTo("isDelete",order.getIsDelete());
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
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Order
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 增加Order
     * @param order
     */
    @Override
    public void add(Order order){
        orderMapper.insert(order);
    }

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Order全部数据
     * @return
     */
    @Override
    public List<Order> findAll() {
        List orderInfo = redisTemplate.boundHashOps("OrderInfo").values();
        return orderInfo;
    }

    /**
     * 新增订单
     * @param order
     */
    @GlobalTransactional  //引用seata，开启全局分布式事务
    @Override
    public void addOrder(Order order) {
        //日志打印
        OrderLog orderLog = new OrderLog();
        orderLog.setOperateTime(new Date());
        orderLog.setOrderId("1");
        orderLogService.update(orderLog);
        //订单id
        order.setId("Order"+idWorker.nextId());
        //用户名
        String username = order.getUsername();
        Integer num = 0;  //数量
        Integer money = 0; //总价
        //扣减库存数据封装
        Map<String,Object> map = new HashMap<>();
        //获取购物信息，订单详情数据处理
        String[] ids = order.getIds();
        for (String id : ids) {
            OrderItem orderItem = (OrderItem)redisTemplate.boundHashOps("Cart_" + username).get(id);
            //订单详情id
            orderItem.setId("OrderItem"+idWorker.nextId());
            orderItem.setOrderId(order.getId());
            num +=orderItem.getNum();
            money +=orderItem.getMoney();
            map.put(id,orderItem.getNum());
            orderItemMapper.insertSelective(orderItem);
            redisTemplate.boundHashOps("Cart_" + username).delete(id);  //提交后清空购物车的所选商品
        }
        //订单数据处理
            order.setTotalNum(num);
            order.setTotalMoney(money);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            order.setOrderStatus("0");
            order.setPayStatus("0");
            order.setConsignStatus("0");
            order.setIsDelete("0");
            orderMapper.insertSelective(order);
            //调用库存，进行库存扣减
            skuFeign.deCount(map);
            //调用用户。进行积分变更
            userFeign.addPoint(1000);
            //订单数据存入redis中
        //redisTemplate.boundValueOps(order.getId()).set(order);//生成多个key
        redisTemplate.boundHashOps("OrderInfo_").put(order.getId(),order);
        //设置延迟队列，处理超时未支付的订单
        rabbitTemplate.convertAndSend("dead_queue", (Object) JSONObject.toJSONString(order), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setExpiration("1800000");
                return message;
            }
        });
    }
    /**
     * 支付成功--在监听器中调用
     * @param orderId
     * @param transaction_id
     */
    @Override
    public void updateOrder(String orderId, String transaction_id) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order.getPayStatus().equalsIgnoreCase("0")){
            order.setPayStatus("1");
            order.setUpdateTime(new Date());
            order.setPayTime(new Date());
            order.setTransactionId(transaction_id);
            orderMapper.updateByPrimaryKeySelective(order);
            redisTemplate.boundHashOps("OrderInfo_").put(order.getId(),order);
        }
    }
    /**
     * 支付失败删除订单--在监听器中调用
     * @param orderId
     */
    @Override
    public void deleteOrder(String orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order.getPayStatus().equalsIgnoreCase("0")){
            order.setPayStatus("2");
            order.setUpdateTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            redisTemplate.boundHashOps("OrderInfo_").put(order.getId(),order);
            //回滚库存数据
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            List<OrderItem> select = orderItemMapper.select(orderItem);
            HashMap<String, Object> map = new HashMap<>();
            for (OrderItem item : select) {
                map.put(item.getSkuId(),item.getNum());
            }
            skuFeign.rollBack(map);
        }
    }
}
