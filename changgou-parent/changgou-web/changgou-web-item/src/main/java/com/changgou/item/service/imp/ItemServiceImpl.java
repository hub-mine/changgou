package com.changgou.item.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.GoodsFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.ItemService;
import com.changgou.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
@Service
public class ItemServiceImpl implements ItemService  {
    @Autowired
    private GoodsFeign goodsFeign;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private CategoryFeign categoryFeign;
    @Value("${pagepath}")
    private String pagePath;
    /**
     * 所有页面
     */
    @Override
    public void createHtml()throws Exception {

        //获取所有有效商品列表信息
        Result<List<Goods>> allGoods = goodsFeign.findAllGoods();
        List<Goods> goodsList = allGoods.getData();
        //获取spu信息
        for (Goods goods : goodsList) {
            createHtmlModel(goods);
        }
        //信息存入model，提供页面
    }

    /**
     * 指定页面
     * @param id
     */
    @Override
    public void createHtml(String id) throws Exception{
        //获取指定商品信息
        Result<Goods> goodsById = goodsFeign.findGoodsById(id);
        Goods goods = goodsById.getData();
        createHtmlModel(goods);
    }
    /**
     * 生成静态页面
     * @param goods
     */
    private void createHtmlModel(Goods goods) throws Exception {
        //存入数据容器对象
        Context context = new Context();
        Spu spu = goods.getSpu();
        List<Sku> skuList = goods.getSkuList();
        context.setVariable("skuList",skuList);
        //获取信息
        Integer category1Id = spu.getCategory1Id();
        Category category1 = categoryFeign.findById(category1Id).getData();
        context.setVariable("category1",category1);
        Integer category2Id = spu.getCategory2Id();
        Category category2 = categoryFeign.findById(category2Id).getData();
        context.setVariable("category2",category2);
        Integer category3Id = spu.getCategory3Id();
        Category category3 = categoryFeign.findById(category3Id).getData();
        context.setVariable("category3",category3);
        //图片信息
        String images = spu.getImages();
        context.setVariable("imageList",images.split(","));
        //规格数据
        String specItems = spu.getSpecItems();
        Map specMap = JSONObject.parseObject(specItems, Map.class);
        context.setVariable("specMap",specMap);
        //写入对象声明
        File file = new File(pagePath,spu.getId()+".html");
        PrintWriter printWriter = new PrintWriter(file,"UTF-8");
        //指定生成页面的模板
        templateEngine.process("item",context,printWriter);
    }
}
