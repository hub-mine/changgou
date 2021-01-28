package com.changgou.web.controller;

import com.changgou.search.feign.SearchFeign;
import com.changgou.util.Page;
import com.changgou.util.Result;
import com.changgou.util.UrlUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Controller
@CrossOrigin
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchFeign searchFeign;
    @Value(value = "${searchUrl}")
    private String searchUrl;
    /**
     * 搜索页面数据展示
     */
    @RequestMapping("/list")
    public String search(Model model,@RequestParam(required = false) Map<String,String> data){
        Result<Map<String, Object>> result = searchFeign.search(data);
        if(!result.isFlag()){
            throw new RuntimeException("系统异常");
        }
        Map<String, Object> resultData = result.getData();
        model.addAttribute("resultData",resultData);
        model.addAttribute("searchMap",data);
        //url放入
        String url = getUrl(data);
        model.addAttribute("url",url);
        /**
         * 使用url的工具处理排序url
         */
        String SUrl = UrlUtils.replateUrlParameter(url, "softField", "softRule","pageNum");
        model.addAttribute("url",UrlUtils.replateUrlParameter(url,"pageNum"));
        model.addAttribute("SUrl",SUrl);
        //分页信息数据
        Object totalElements = resultData.get("totalElements");
        Object pageNumber = resultData.get("pageNumber");
        Object pageSize = resultData.get("pageSize");
        Page<Object> page = new Page<Object>(Long.valueOf(totalElements.toString()),Integer.parseInt(pageNumber.toString()), Integer.parseInt(pageSize.toString()));
        model.addAttribute("page",page);
        model.addAttribute("searchUrl", searchUrl);
        return "search";
    }
    /**
     * 对url的处理
     */
    private String getUrl(@RequestParam(required = false) Map<String,String> data){
        String url="/search/list?";
        //对查询参数进行处理
        for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {
            String value = stringStringEntry.getValue();
            String key = stringStringEntry.getKey();
            url=url+key+"="+value+"&";
        }
        return url.substring(0,url.length()-1);  //处理掉最后的"&"
    }
}
