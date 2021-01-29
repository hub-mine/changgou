package com.changgou.service.imp;

<<<<<<< HEAD
import com.alibaba.fastjson.JSONObject;
=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
import com.changgou.service.PayService;
import com.changgou.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.util.StringUtils;
=======
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e

import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.notifyurl}")
    private String notifyurl;
    /**
     * 从微信查询订单信息
     * @param orderId
     * @return
     */
    @Override
    public Map<String, String> checkOrder(String orderId) {
        try {
            //定义请求微信下单的url
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            //请求参数:根据api文档封装数据
            HashMap<String, String> map = new HashMap<>();
            map.put("appid", appid);
            map.put("mch_id", partner);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("out_trade_no", orderId);
            String xml = WXPayUtil.generateSignedXml(map, partnerkey);  //转成xml同时拥有sign
            //发起请求,使用httpclient工具请求
            HttpClient httpClient = new HttpClient(url);
            httpClient.setXmlParam(xml);
            httpClient.setHttps(true);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            //获取请求结果
            String return_code = resultMap.get("return_code");
            if(return_code.equalsIgnoreCase("SUCCESS")){
                String result_code = resultMap.get("result_code");
                if(result_code.equalsIgnoreCase("SUCCESS")){
                    //获取 支付二维码连接
                   return resultMap;
                }
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 调用微信的下单api生成下单二维码
<<<<<<< HEAD
     * @param data
     * @return
     */
    @Override
    public String createCode(Map<String,Object>data) {
=======
     * @param orderId
     * @param money
     * @return
     */
    @Override
    public String createCode(String orderId, Integer money) {
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
        try {
            //定义请求微信下单的url
            String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
            //请求参数:根据api文档封装数据
            HashMap<String, String> map = new HashMap<>();
            map.put("appid",appid);
            map.put("mch_id",partner);
            map.put("notify_url",notifyurl);;
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body","测试");
<<<<<<< HEAD
            map.put("out_trade_no",data.get("orderId").toString());
            map.put("total_fee",data.get("money").toString());
            map.put("spbill_create_ip","192.168.211.1");
            map.put("trade_type","NATIVE");
            //定义附件参数，加入exchangehe routingKey
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("exchange",data.get("exchange"));
            map1.put("routingKey",data.get("routingKey"));
            Object username = data.get("username");   //秒杀订单在redis中，需要用名字取出
            if(!StringUtils.isEmpty(username)){
                map1.put("username",username);
            }
            map.put("attach", JSONObject.toJSONString(map1));
=======
            map.put("out_trade_no",orderId);
            map.put("total_fee",money.toString());
            map.put("spbill_create_ip","192.168.211.1");
            map.put("trade_type","NATIVE");
>>>>>>> 7b205edc9e439a46f42fe64cf86df176e02ff97e
            //参数转xml格式。同时添加签名
            String xml = WXPayUtil.generateSignedXml(map, partnerkey);  //转成xml同时拥有sign
            //发起请求,使用httpclient工具请求
            HttpClient httpClient = new HttpClient(url);
            httpClient.setXmlParam(xml);
            httpClient.setHttps(true);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            //获取请求结果
            String return_code = resultMap.get("return_code");
            if(return_code.equalsIgnoreCase("SUCCESS")){
                String result_code = resultMap.get("result_code");
                if(result_code.equalsIgnoreCase("SUCCESS")){
                    //获取 支付二维码连接
                    String url1= resultMap.get("code_url");
                    return url1;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
