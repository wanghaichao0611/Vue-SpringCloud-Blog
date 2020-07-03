package com.es.Controller;

import com.es.EsRequset.EsRequest;
import com.es.EsResponse.CategoryEsResponse;
import com.es.EsResponse.EsResponse;
import com.es.Service.EsService;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EsArticleController {

    //ES的服务
    @Autowired
    EsService esService;

    //创建索引并且添加文章，若存在指定的索引则直接插入
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/addArticle")
    public Map<String, Object> addArticle(@RequestBody EsRequest esRequest){
        Map<String, Object> resultMap=new HashMap<>();
        esService.addArticle(esRequest);
        resultMap.put("success",true);
        return resultMap;
    }

    //删除Es中文章的Id
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/deleteArticle")
    public Map<String, Object> deleteArticle(@RequestBody EsRequest esRequest){
        Map<String, Object> resultMap=new HashMap<>();
        esService.deleteArticle(esRequest);
        resultMap.put("success",true);
        return resultMap;
    }

    //更新Es中文章的信息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/updateArticle")
    public Map<String, Object> updateArticle(@RequestBody EsRequest esRequest,@RequestParam("oldCategoryName")String oldCategoryName){
        Map<String, Object> resultMap=new HashMap<>();
        esService.updateArticle(esRequest,oldCategoryName);
        resultMap.put("success",true);
        return resultMap;
    }

    //第一个主页的搜索(page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/esPageAll")
    public Map<String,Object> esPageAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "count", defaultValue = "5") int count,
                                        @RequestParam("categoryName") String categoryName,
                                        @RequestParam("keyWord") String  keyWord){
        //关键字搜索标题和摘要
        Map<String,Object> esResponseMap=esService.firstHomePageEsPage(page,count,categoryName,keyWord);
        esResponseMap.put("success",true);
        return esResponseMap;
    }

    //博客分类的Es搜索
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/esCategoryAll")
    public Map<String,Object> esCategoryAll(@RequestParam("categoryName") String categoryName){
        Map<String, Object> resultMap=new HashMap<>();
        //ES分类搜索
        List<CategoryEsResponse> categoryEsResponseList=esService.categoryHighEs(categoryName);
        resultMap.put("success",true);
        resultMap.put("list",categoryEsResponseList);
        return resultMap;
    }
}