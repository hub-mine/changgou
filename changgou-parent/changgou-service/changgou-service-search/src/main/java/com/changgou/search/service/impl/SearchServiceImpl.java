package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.dao.SearchDao;
import com.changgou.search.service.SearchService;
import com.changgou.util.PageResult;
import com.changgou.util.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SearchDao searchDao;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    /**
     * 导入数据
     */
    @Override
    public void importData() {
        //调用获取所有商品数据
        Result<List<Sku>> listResult = skuFeign.findByStatus("1");
        if(!listResult.isFlag()){
            throw  new RuntimeException("查询异常");
        }
        //商品列表
        List<Sku> data = listResult.getData();
        //数据处理
        List<SkuInfo> skuInfos = JSONObject.parseArray(JSONObject.toJSONString(data), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            if(skuInfo.getSpec()!=null){
                //将spec转换成map类型
                Map<String,Object> specMap = JSONObject.parseObject(skuInfo.getSpec(), Map.class);
                skuInfo.setSpecMap(specMap);
            }
        }
            searchDao.saveAll(skuInfos);
    }
    /**
     * 关键字查询
     */
    @Override
    public Map<String, Object> search(Map<String, String> data) {
        //初始化返回对象
        Map<String, Object> resultMap = new HashMap<>();
        //调用优化方法:构造条件
        NativeSearchQueryBuilder builder = builderQuery(data);
//        //执行搜索
//        List<SkuInfo> content = search(builder);
//        //商品数据
//        resultMap.put("rows",content);
        //执行搜索
        resultMap = search(builder,data);  //聚合数据和商品数据在同一方法中查询获取，设置只需要查询一次数据
        //商品数据
//        //类别统计
//        //判断条件是否有类别
//        String category = data.get("category");
//        if (StringUtils.isEmpty(category)) {
//            List<String> strings = searchCategory(builder);
//            resultMap.put("categorys",strings);
//        }
//        //品牌统计
//        //判断条件是否有品牌
//        String brand = data.get("brand");
//        if (StringUtils.isEmpty(brand)) {
//            List<String> brands = searchBrand(builder);
//            resultMap.put("brands",brands);
//        }
        //类别和品牌以及规格的聚合查询优化
//        Map<String, Object> aggResult = getAggResult(builder, data);
//        resultMap.putAll(aggResult);
//        //规格统计
//        Map<String, Set<String>> stringSetMap = searchSpec(builder);
//        resultMap.put("specList",stringSetMap);
        return resultMap;
    }
    /**
     * 类别的聚合查询
     */
    public List<String> searchCategory(NativeSearchQueryBuilder builder){
        //条件构造
        builder.addAggregation(AggregationBuilders.terms("categorys").field("categoryName"));
        //执行查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取分组数据
        Aggregations aggregations = skuInfos.getAggregations();
        //指定获取分类统计数据
        StringTerms categorys = aggregations.get("categorys");
        //数据封装
        ArrayList<String> strings = new ArrayList<>();
        List<StringTerms.Bucket> buckets = categorys.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            strings.add(bucket.getKeyAsString());
        }
        return strings;
    }
    /**
     * 品牌聚合查询
     */
    public List<String> searchBrand(NativeSearchQueryBuilder builder){
        //条件构造
        builder.addAggregation(AggregationBuilders.terms("brands").field("brandName").size(10000));
        //执行查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取分组数据
        Aggregations aggregations = skuInfos.getAggregations();
        //指定获取分类统计数据
        StringTerms brand = aggregations.get("brands");
        //数据封装
        ArrayList<String> brands = new ArrayList<>();
        List<StringTerms.Bucket> buckets = brand.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            brands.add(bucket.getKeyAsString());
        }
        return brands;
    }
    /**
     * 规格的聚合查询：规格类型;Map<String,Set<String></>>
     */
    public Map<String, Set<String>> searchSpec(NativeSearchQueryBuilder builder){
        //初始化返回值
        Map<String,Set<String>> specMap=new HashMap<>();
        //条件构造
        builder.addAggregation(AggregationBuilders.terms("spec").field("spec.keyword").size(10000));
        //执行查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取分组数据
        Aggregations aggregations = skuInfos.getAggregations();
        //指定获取分类统计数据
        StringTerms aggregation = aggregations.get("spec");
        //数据封装
        for (StringTerms.Bucket bucket : aggregation.getBuckets()) {
            //获取数据
            String keyAsString = bucket.getKeyAsString();
            //数据转化成JSON 类型
            Map<String,String> map = JSONObject.parseObject(keyAsString, Map.class);
            //进行循环
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                String key = stringStringEntry.getKey();
                String value = stringStringEntry.getValue();
                Set<String> strings = specMap.get(key);
                if(strings==null){
                    strings=new HashSet<>();
                }
                strings.add(value);
                specMap.put(key,strings);
            }
        }
        return specMap;
    }
    /**
     * 代码优化部分：条件构造
     * 条件新增多条件查询
     */
    public NativeSearchQueryBuilder builderQuery(Map<String, String> data){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //多条件查询实现
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(data !=null && data.size()>0){
            String keywords = data.get("keywords");
            if(!StringUtils.isEmpty(keywords)){
                //builder.withQuery(QueryBuilders.matchQuery("name",keyword));
                boolQueryBuilder.must(QueryBuilders.matchQuery("name",keywords));
            }
            //类别条件
            String category = data.get("category");
            if(!StringUtils.isEmpty(category)){
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName",category));
            }
            //品牌条件
            String brand = data.get("brand");
            if(!StringUtils.isEmpty(brand)){
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName",brand));
            }
            //规格条件
            for (Map.Entry<String, String> stringStringEntry : data.entrySet()) {
                if(stringStringEntry.getKey().startsWith("spec_")){
                    //规格参数数据处理
                    String key = stringStringEntry.getKey().replace("spec_", "");
                    String value = stringStringEntry.getValue();
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap."+key+".keyword",value));
                }
            }
            //价格查询
            String price = data.get("price");
            if(!StringUtils.isEmpty(price)){
                //数据处理
                String replace = price.replace("元", "").replace("以上", "");
                String[] prices = replace.split("-");
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(prices[0]));
                if(prices.length>1){
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(prices[0]).lt(prices[1]));
                }
            }
            //将条件组合起来
            builder.withQuery(boolQueryBuilder);
            //页面排序实现
            String softField = data.get("softField");
            String softRule = data.get("x");
            if(!StringUtils.isEmpty(softField)&&!StringUtils.isEmpty(softRule)){
                builder.withSort(SortBuilders.fieldSort(softField).order(SortOrder.valueOf(softRule)));
            }else {
                //自定义默认排序按照价格的降序排序
                builder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
            }
            //分页查询实现
            Integer pageSize = 20 ; //每一页显示数量
            //前端选择第几页
            Integer pageNum = getPage(data.get("pageNum"));
            //进行分页处理
            builder.withPageable(PageRequest.of(pageNum-1,pageSize));  //es数据展示从0开始，所以第一页是0

            //高亮查询
            //定义高亮域
            HighlightBuilder.Field field= new HighlightBuilder
                                            .Field("name")
                                            .preTags("<font style='color:red'>")
                                            .postTags("</font>").fragmentSize(100); //定义100个字符内高亮处理
            //设置高亮,再去查询方法中获取高亮的数据
            builder.withHighlightFields(field);
        }
        //设置聚合条件
        String category = data.get("category");
        String brand = data.get("brand");
        if(StringUtils.isEmpty(category)){
            builder.addAggregation(AggregationBuilders.terms("category").field("categoryName").size(100000));
        }
       if(StringUtils.isEmpty(brand)){
           builder.addAggregation(AggregationBuilders.terms("brand").field("brandName").size(100000));
       }
        builder.addAggregation(AggregationBuilders.terms("spec").field("spec.keyword").size(100000));
        return builder;
    }
    /**
     * 页码处理
     */
    private Integer getPage(String pageNum){
        try {
            int i = Integer.parseInt(pageNum);
            return i;
        }catch (Exception e){
            return 1;
        }
    }
    /**
     * 代码优化：提取商品列表搜索方法
     * 增加高亮显示数据提取
     */
    public Map<String,Object> search(NativeSearchQueryBuilder builder,Map<String, String> data){
        //执行查询
        AggregatedPage<SkuInfo> skuInfos =
                elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class, new SearchResultMapper() {
                    @Override
            /**
             * 获取高亮数处理
             */
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        //定义一个集合存储所有高亮数据
                        List<T> list = new ArrayList<T>();
                            //获取命中数据
                        SearchHits hits = searchResponse.getHits();
                        //获取聚合前数据
                        Aggregations aggregations = searchResponse.getAggregations();
                        //循环所有数据
                        for (SearchHit hit :hits ) {
                            //获取所有数据
                            SkuInfo skuInfo = JSONObject.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            //获取高亮数据
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                           if(highlightFields !=null && highlightFields.size()>0){
                               HighlightField field = highlightFields.get("name");
                               Text[] fragments = field.getFragments();
                               String name = "";
                               for (Text fragment : fragments) {
                                   name +=fragment;
                               }
                               //将非高亮数据替换成高亮数据
                               skuInfo.setName(name);
                           }
                            //将高亮数据返回
                            list.add((T) skuInfo);
                        }
                        //1：返回的集合数据   2：分页数据   3：总记录数  4.高亮处理前的商品数据放回，防止后面的聚合获取不到数据
                        return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(),aggregations);
            }
        });
        //初始化返回值
        Map<String,Object> map = new HashMap<>();
        //获取结果返回
        List<SkuInfo> content = skuInfos.getContent();
        //商品数据
        map.put("rows",content);
        //数据聚合处理
        String category = data.get("category");
        String brand = data.get("brand");
        Aggregations aggregations = skuInfos.getAggregations();
        if(StringUtils.isEmpty(category)){
            List<String> categoryList = getResult(aggregations, "category");
            map.put("categoryList",categoryList);
        }
        if(StringUtils.isEmpty(brand)){
            List<String> brandList = getResult(aggregations, "brand");
            map.put("brandList",brandList);
        }
        List<String> specList = getResult(aggregations, "spec");
        Map<String, Set<String>> spec = getSpec(specList);
        //前台分页数据处理
        long totalElements = skuInfos.getTotalElements();
        int pageNumber = skuInfos.getPageable().getPageNumber()+1;
        int pageSize = skuInfos.getPageable().getPageSize();
        map.put("totalElements",totalElements);
        map.put("pageNumber",pageNumber);
        map.put("pageSize",pageSize);
        map.put("spec",spec);
        return map;
    }
    /**
     * 代码优化：整合类别+品牌+规格的聚合统计
     */
    public Map<String,Object> getAggResult(NativeSearchQueryBuilder builder,Map<String,String> data){
        Map<String,Object> map = new HashMap<>();
        //判断是否进行聚合
        String category = data.get("category");
        String brand1 = data.get("brand");
        //条件构造
        if(StringUtils.isEmpty(category)){
            builder.addAggregation(AggregationBuilders.terms("category").field("categoryName").size(10000));
        }
        if(StringUtils.isEmpty(brand1)){
            builder.addAggregation(AggregationBuilders.terms("brand").field("brandName").size(10000));
        }
        builder.addAggregation(AggregationBuilders.terms("spec").field("spec.keyword").size(10000));
        //执行查询
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        //获取分组数据
        Aggregations aggregations = skuInfos.getAggregations();
       if(StringUtils.isEmpty(category)){
           List<String> categoryList = getResult(aggregations, "category");
           map.put("categoryList",categoryList);
       }
        if(StringUtils.isEmpty(brand1)){
            List<String> brandList = getResult(aggregations, "brand");
            map.put("brandList",brandList);
        }
        List<String> specList = getResult(aggregations, "spec");
        Map<String, Set<String>> spec = getSpec(specList);
        map.put("spec",spec);
        return map;
    }
    /**
     * 定义方法处理规格的重合数据
     */
    private Map<String,Set<String>> getSpec(List<String> specList){
        Map<String, Set<String>> specMap = new HashMap<>();
        for (String spec : specList) {
            //数据转化成JSON 类型
            Map<String,String> map = JSONObject.parseObject(spec, Map.class);
            //进行循环
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                String key = stringStringEntry.getKey();
                String value = stringStringEntry.getValue();
                Set<String> strings = specMap.get(key);
                if(strings==null){
                    strings=new HashSet<>();
                }
                strings.add(value);
                specMap.put(key,strings);
            }
        }

        return specMap;
    }
    /**
     * 获取聚合查询的结果
     */
    private List<String> getResult(Aggregations aggregations ,String name){
        List<String> list = new ArrayList<>();
        //循环遍历
        if(aggregations != null){
            StringTerms stringTerms = aggregations.get(name);
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();
                list.add(keyAsString);
            }
        }
        return list;
    }
}
