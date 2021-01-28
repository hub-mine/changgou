package com.changgou.canal.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.util.Result;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@CanalEventListener  //监听数据库，触发类中的方法
public class CanalDataEventListener {
    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 新增
     *rowData:变化的数据
     * eventType ：变化类型
     */
    @InsertListenPoint //标识新增监听
    public void insertListener(CanalEntry.RowData rowData){
        //变化后的数据类型
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            //列的名字
            String name = column.getName();
            if(name.equalsIgnoreCase("category_id")){
                String value = column.getValue();
                //调用content接口查询数据库中content列表
                Result<List<Content>> byCategoryId = contentFeign.findByCategoryId(Long.valueOf(value));
                //将获得数据转换String类型
                String s = JSONObject.toJSONString(byCategoryId);
                //存储数据到redis中
                stringRedisTemplate.boundValueOps("content_"+value).set(s);

            }
        }
    }
    /**
     * 修改事件
     */
    @UpdateListenPoint
    public void UpdateListener(CanalEntry.RowData rowData){
            //修改前的数据
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        //修改后的数据
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
    }

    /**
     * 删除事件
     * @param rowData
     */
    @DeleteListenPoint
    public void deleteListener(CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
    }

    /**
     * 自定义监听
     * @param rowData
     * @param type
     */
    @ListenPoint(destination = "example",schema = "changgou=content",table = "tb_content",eventType = {CanalEntry.EventType.DELETE,
                                CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE})
    public void defaultListener(CanalEntry.RowData rowData,CanalEntry.EventType type){

    }
}
