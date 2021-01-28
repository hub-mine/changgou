package com.changgou.item.controller;

import com.changgou.item.service.ItemService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {
    @Autowired
    private ItemService itemService;
    @GetMapping("/create/{id}")
    public Result creat(@PathVariable(value = "id") String id) throws Exception{
        itemService.createHtml(id);
        return new Result(true, StatusCode.OK,"创建一个成功");
    }
    @GetMapping("/create")
    public Result creat() throws Exception{
        itemService.createHtml();
        return new Result(true, StatusCode.OK,"创建所有成功");
    }
}
