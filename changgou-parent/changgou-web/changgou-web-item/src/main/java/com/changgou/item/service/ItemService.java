package com.changgou.item.service;

public interface ItemService {
    /**
     * 所有商品静态页面
     */
    void createHtml()throws Exception;
    /**
     * 指定商品的静态页面
     */
    void createHtml(String id)throws Exception;
}
