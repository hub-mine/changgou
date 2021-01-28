package com.changgou.controller;

import com.alibaba.fastjson.JSONObject;
import com.changgou.service.PayService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weixin/pay")
public class PayController {
    @Autowired
    private PayService payService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 获取支付二维码连接
     */
    @RequestMapping("/create/native")
    public Result create(String orderId,Integer money){
        String code = payService.createCode(orderId, money);
        return new Result(true, StatusCode.OK,"生成地址",code);
    }
    @RequestMapping("/check")
    public Result check(String orderId){
        Map<String, String> map = payService.checkOrder(orderId);
        return new Result(true,StatusCode.OK,"查询成功",map);
    }
    /**
     * 微信回调方法
     */
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request){
        try {
            ServletInputStream inputStream = request.getInputStream();
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            byte[] buff=new byte[1024];
            int len =0;
            while ((len=inputStream.read(buff)) !=-1){
                io.write(buff,0,len);
            }
            byte[] bytes = io.toByteArray();
            String s = new String(bytes, "UTF-8");
            Map<String, String> result = WXPayUtil.xmlToMap(s);  //微信返回结果
            //将结果内容发送到消息队列中
            rabbitTemplate.convertAndSend("order_exchange","pay.#", JSONObject.toJSONString(result));
            io.close();
            inputStream.close();
            //定义返回微信的结果
            HashMap<String, String> map = new HashMap<>();
            map.put("return_code","SUCCESS");
            map.put("return_msg","OK");
            return WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            return null;
        }

    }
}
