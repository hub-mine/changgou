package com.changgou.order.controller;

import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import com.changgou.order.util.TokenDecode;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    /**
     * 新增购物车
     */
    @GetMapping("/addCart")
    public Result addCart(String skuId,Integer num){
        //cartService.add(skuId,num,"zhangsan");
        //调用工具类从令牌中获取用户信息
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String username = userInfo.get("username");
        cartService.add(skuId,num,username);
        return new Result(true, StatusCode.ACCESSERROR,"新增成功");
    }
    /**
     * 查询购物车列表
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result list(){
        //String username="zhangsan";
        //调用工具类从令牌中获取用户信息
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String username = userInfo.get("username");
        List<OrderItem> list = cartService.list(username);
        return new Result(true,StatusCode.ACCESSERROR,"查询成功",list);
    }
    /**
     * 查询购物车列表
     * @param
     * @return
     */
    @GetMapping("/list/choose")
    public Result list(@RequestParam String[] ids){
        //String username="zhangsan";
        //调用工具类从令牌中获取用户信息
        Map<String, String> userInfo = TokenDecode.getUserInfo();
        String username = userInfo.get("username");
        List<OrderItem> list = cartService.list(username,ids);
        return new Result(true,StatusCode.ACCESSERROR,"查询成功",list);
    }
}
