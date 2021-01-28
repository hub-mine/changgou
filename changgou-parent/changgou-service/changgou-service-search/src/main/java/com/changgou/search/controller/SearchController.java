package com.changgou.search.controller;

import com.changgou.search.service.SearchService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    /**
     * 关键字查询
     */
    @GetMapping
    public Result<Map<String,Object>> search(@RequestParam Map<String,String>data){
        Map<String, Object> search = searchService.search(data);
        return new Result(true,StatusCode.OK,"查询成功",search);
    }
    /**
     * 导入数据
     */
    @GetMapping("/import")
    public Result importData(){
        searchService.importData();
        return new Result(true, StatusCode.OK,"成功");
    }
}
