package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
<<<<<<< HEAD
import org.springframework.data.redis.core.StringRedisTemplate;
=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 设置定时任务定时从数据库中拉取商品数据存入到redis中
 */
@Component
public class SeckillGoodsTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
<<<<<<< HEAD
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
    @Scheduled(cron =" 5 * * * * *")
    public void pullGoods(){
       // System.out.println(System.currentTimeMillis()+"秒杀任务");测试代码
        List<Date> dateMenus = DateUtil.getDateMenus();  //获取时间段
        for (Date dateMenu : dateMenus) {
            //时间格式转换
            String startTime = DateUtil.data2str(dateMenu, "yyyy-MM-dd HH:mm:ss");
            Date date = DateUtil.addDateHour(dateMenu, 2);
            String endTime = DateUtil.data2str(date, "yyyy-MM-dd HH:mm:ss");
            //构建查询条件
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //商品审核通过
            criteria.andEqualTo("status","1");
            //库存大于0
            criteria.andGreaterThan("stockCount",0);
            //开始时间小于等于活动开始时间
            criteria.andGreaterThanOrEqualTo("startTime",startTime);
            //开始时间+2小时大于活动结束时间
            criteria.andLessThanOrEqualTo("endTime",endTime);
            //排除已经缓存到秒杀的缓存商品数据
<<<<<<< HEAD
            String time = DateUtil.data2str(dateMenu,DateUtil.PATTERN_YYYYMMDDHH);
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + time).keys();
=======
            String s = DateUtil.data2str(dateMenu,DateUtil.PATTERN_YYYYMMDDHH);
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + s).keys();
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
            if(keys!=null&&keys.size()>0){
                for (Object key : keys) {
                    criteria.andNotIn("id",keys);
                }
            }
            //按条件查询数据库，将数据存入redis中
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            if(seckillGoods!=null&&seckillGoods.size()>0){
                for (SeckillGoods seckillGood : seckillGoods) {
<<<<<<< HEAD
                    redisTemplate.boundHashOps("SeckillGoods_" + time).put(seckillGood.getId(),seckillGood);
                    //处理超卖问题，将库存放入redis中，关联下单进行减库存
                    //引用redis的list数据类型特性
                    String[] ids=getIds(seckillGood);
                    stringRedisTemplate.boundListOps("SeckillGoodsQueue_"+seckillGood.getId()).leftPushAll(ids);
                    stringRedisTemplate.boundHashOps("SeckillStockCount").increment(seckillGood.getId(),seckillGood.getStockCount());
=======
                    redisTemplate.boundHashOps("SeckillGoods_" + s).put(seckillGood.getId(),seckillGood);
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
                }
            }
        }
    }
<<<<<<< HEAD

    /**
     * 创建秒杀商品的id集合
     * @param seckillGoods
     * @return
     */
    private String[] getIds(SeckillGoods seckillGoods ){
        String[] ids=new String[seckillGoods.getStockCount()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = seckillGoods.getId();
        }
        return ids;
    }
=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
}
