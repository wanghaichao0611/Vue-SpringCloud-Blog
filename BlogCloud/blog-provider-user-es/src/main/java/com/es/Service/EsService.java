package com.es.Service;

import com.es.EsRequset.EsRequest;
import com.es.EsResponse.CategoryEsResponse;
import com.es.EsResponse.EsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EsService {

    //放入Es索引
    void addArticle(EsRequest esRequest);

    //删除Es中的指定Id文章
    void deleteArticle(EsRequest esRequest);

    //更新Es中的文章信息
    void updateArticle(EsRequest esRequest,String oldCategoryName);

    //第一个主页的分页搜索(page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数)
    Map<String,Object> firstHomePageEsPage(int page, int count, String categoryName, String keyWord);

    //ES分类搜索
    List<CategoryEsResponse> categoryHighEs(String categoryName);
}
