package com.changgou.search.service;

import java.util.Map;

public interface SearchService {
    /**
     * 导入数据
     */
    void importData();
    /**
     * 关键字查询
     */
    Map<String,Object> search(Map<String,String> data);
}
