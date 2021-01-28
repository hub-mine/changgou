package com.test;

import com.changgou.util.BCrypt;
import com.changgou.util.HttpClient;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class Test {
    @org.junit.Test
    public void encode(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123");
        System.out.println(encode);


    }
    @org.junit.Test
    public void check(){
        boolean checkpw = BCrypt.checkpw("123","$2a$10$wX6pdupJTZ..LDLYEFrcCuKhuPzPPnmt5QJowp0k62vpO5vdFSVxS");
        System.out.println(checkpw);
    }

    /**
     * 微信支付的工具测试
     */
    @org.junit.Test
    public void wxTest() throws Exception{
        String s = WXPayUtil.generateNonceStr();  //自动生成字符串
        System.out.println(s);
        HashMap<String, String> map = new HashMap<>();
        map.put("zs","1");
        String s1 = WXPayUtil.mapToXml(map);        //map类型转xml格式
        System.out.println(s1);
        //xml转换map
        Map<String, String> stringStringMap = WXPayUtil.xmlToMap(s1);
        System.out.println(stringStringMap);
    }

    /**
     * 外部调用工具：httpclient测试
     * @throws Exception
     */
    @org.junit.Test
    public void testHttp() throws Exception{
        String url="https://www.baidu.com";
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.get();
        String content = httpClient.getContent();
        System.out.println(content);
    }
}
