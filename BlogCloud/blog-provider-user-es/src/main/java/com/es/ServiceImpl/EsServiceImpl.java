package com.es.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.es.EsRequset.EsRequest;
import com.es.EsResponse.CategoryEsResponse;
import com.es.EsResponse.EsResponse;
import com.es.Service.EsService;
import com.es.util.EsDate;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class EsServiceImpl implements EsService {

    @Autowired
    private TransportClient transportClient;

    private static final Logger LOGGER= LoggerFactory.getLogger(EsServiceImpl.class);

    //放入Es索引
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void addArticle(EsRequest esRequest) {
        try {
            //构建JSON对象
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                    .field("articleId",esRequest.getArticleId())
                    .field("articleTitle", esRequest.getTitle())
                    .field("summary", esRequest.getSummary())
                    .field("articlePublishDate", EsDate.getDateFormatter(esRequest.getArticlePublishDate()))
                    .field("articleUpdateDate", EsDate.getDateFormatter(esRequest.getArticleUpdateDate()))
                    .endObject();
            //执行索引操作。
            IndexResponse addResponse=transportClient.prepareIndex(esRequest.getCategory().toLowerCase(),"article",esRequest.getArticleId()+"")
                    .setSource(builder).get();
            LOGGER.info("插入的结果:"+addResponse.getResult());
        }catch (IOException e){
            LOGGER.info("插入失败!");
        }
    }

    //删除Es上面指定的文章
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteArticle(EsRequest esRequest) {
        try {
            //删除Es中文章Id的所有信息
            DeleteResponse deleteResponse = transportClient.prepareDelete(esRequest.getCategory().toLowerCase(), "article", esRequest.getArticleId() + "")
                    .get();
            LOGGER.info("删除结果:" + deleteResponse.getResult());
        }catch (Exception e){
            LOGGER.info("删除微服务出现网络故障");
        }
    }

    //更新Es中的文章信息
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateArticle(EsRequest esRequest,String oldCategoryName) {
        //查询旧索引中存在的文章的信息(保证的是私密文章转换成公开文章)
        GetResponse firstResponse=transportClient.prepareGet(oldCategoryName.toLowerCase(),"article",esRequest.getArticleId()+"")
                .get();
        if (firstResponse.getSource()==null){
            //把私密改公开的文章直接放入ES中
            try {
                //构建JSON对象
                XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                        .field("articleId",esRequest.getArticleId())
                        .field("articleTitle", esRequest.getTitle())
                        .field("summary", esRequest.getSummary())
                        .field("articlePublishDate", EsDate.getDateFormatter(new Date()))
                        .field("articleUpdateDate", EsDate.getDateFormatter(esRequest.getArticleUpdateDate()))
                        .endObject();
                //执行索引操作。
                IndexResponse addResponse=transportClient.prepareIndex(esRequest.getCategory().toLowerCase(),"article",esRequest.getArticleId()+"")
                        .setSource(builder).get();
                LOGGER.info("插入的结果:"+addResponse.getResult());
            }catch (IOException e){
                LOGGER.info("插入失败!");
            }
        }else if (oldCategoryName.equals(esRequest.getCategory())){
            //如果分类没有发生改变则直接更新信息
            //更新Es中的文章的信息,修改时间等。
            try{
                XContentBuilder builder=XContentFactory.jsonBuilder().startObject()
                        .field("articleTitle", esRequest.getTitle())
                        .field("summary", esRequest.getSummary())
                        .field("articleUpdateDate", EsDate.getDateFormatter(esRequest.getArticleUpdateDate()))
                        .endObject();
                //执行更新,以文章Id为准
                UpdateResponse updateResponse=transportClient.prepareUpdate(esRequest.getCategory().toLowerCase(),"article",esRequest.getArticleId()+"")
                        .setDoc(builder).get();
                LOGGER.info("更新的结果:"+updateResponse.getResult());
            }catch (IOException e){
                LOGGER.info("更新失败!");
            }
        }else {
            //查询旧索引中存在的文章的信息
            GetResponse getResponse=transportClient.prepareGet(oldCategoryName.toLowerCase(),"article",esRequest.getArticleId()+"")
                    .get();
            Map<String,Object> responseMap=new HashMap<>();
            responseMap=getResponse.getSourceAsMap();
            //把首次插入时间获取放到要插入的Es中
            try {
                //构建JSON对象
                XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                        .field("articleId",esRequest.getArticleId())
                        .field("articleTitle", esRequest.getTitle())
                        .field("summary", esRequest.getSummary())
                        .field("articlePublishDate", responseMap.get("articlePublishDate"))
                        .field("articleUpdateDate", EsDate.getDateFormatter(esRequest.getArticleUpdateDate()))
                        .endObject();
                //执行新的索引操作。
                IndexResponse updateAddResponse=transportClient.prepareIndex(esRequest.getCategory().toLowerCase(),"article",esRequest.getArticleId()+"")
                        .setSource(builder).get();
                //最后删除修改之前的Es中的文章
                DeleteResponse updateDeleteResponse=transportClient.prepareDelete(oldCategoryName.toLowerCase(),"article",esRequest.getArticleId()+"")
                        .get();
                LOGGER.info("更新插入的结果:"+updateAddResponse.getResult()+","+"删除的修改之前的结果是:"+updateDeleteResponse.getResult());
            }catch (IOException e){
                LOGGER.info("更新插入失败!");
            }}
    }

    //第一个主页的分页搜索(page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Map<String,Object>  firstHomePageEsPage(int page, int count, String categoryName, String keyWord) {
        Map<String,Object> all=new HashMap<>();
        List<EsResponse> esResponseList=new ArrayList<>();
        //处理特殊字符
        keyWord = QueryParser.escape(keyWord);
        LOGGER.info(keyWord+"");
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field("articleTitle");//高亮articleTitle
        highlightBuilder.field("summary");//高亮summary
        highlightBuilder.requireFieldMatch(false);//如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style=\"color:red\">").postTags("</span>");//高亮标签
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段

        QueryBuilder queryBuilder=QueryBuilders.matchPhraseQuery("articleTitle", keyWord);
        QueryBuilder queryBuilder2=QueryBuilders.matchPhraseQuery("summary", keyWord);
        //条件分页查询
        SearchRequestBuilder searchRequestBuilder=transportClient.prepareSearch(categoryName.toLowerCase())
                .setTypes("article");
        SearchResponse searchResponse=searchRequestBuilder.setQuery(QueryBuilders.boolQuery()
        .should(queryBuilder)
        .should(queryBuilder2))
                //.setFetchSource(new String[]{"title","summary","articlePublishDate","articleUpdateDate"}, null)
                .highlighter(highlightBuilder)
                .addSort("articleId", SortOrder.DESC)
                .setFrom(page)
                .setSize(count)
                .execute()
                .actionGet();
        /**查询关键字的全部数据
         * SearchResponse searchResponse=searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery())
                .highlighter(highlightBuilder)
                .setFrom(page)
                .setSize(count)
                .execute()
                .actionGet();
         **/
        SearchHits hits=searchResponse.getHits();
        EsServiceImpl.LOGGER.info("一共多少条数据:"+hits.getTotalHits());
        /**测试小数据时候用的
         * for(SearchHit hit:hits){
            System.out.println(hit.getSourceAsString());
            System.out.println(hits.getTotalHits());
            System.out.println(hit.getHighlightFields());
            System.out.println(hit.getIndex()+hit.getId());
        }**/
        //循环遍历传给前端
        for (SearchHit hit:hits){
            if (hit!=null){
                Map<String,Object> hitMap=new HashMap<>();
                Map<String, HighlightField> highMap=new HashMap<>();
                hitMap=hit.getSourceAsMap();
                highMap=hit.getHighlightFields();
                HighlightField articleTitleField = highMap.get("articleTitle");
                HighlightField summaryField = highMap.get("summary");
                //千万记得要记得判断是不是为空,不然你匹配的第一个结果没有高亮内容,那么就会报空指针异常。
                if(articleTitleField!=null){
                    org.elasticsearch.common.text.Text[] fragments = articleTitleField.fragments();
                    String articleTitle = "";
                    for (Text text : fragments) {
                        articleTitle=articleTitle+text;
                    }
                    hitMap.put("articleTitle", articleTitle);   //高亮字段替换掉原本的内容
                }
                if(summaryField!=null){
                    Text[] fragments = summaryField.fragments();
                    String summary = "";
                    for (Text text : fragments) {
                        summary=summary+text;
                    }
                    hitMap.put("summary", summary);   //高亮字段替换掉原本的内容
                }
                //添加数据并传给前端显示
                EsResponse esResponse=new EsResponse(Long.valueOf(hit.getId()),categoryName,hitMap.get("articleTitle").toString(),
                        hitMap.get("summary").toString(),EsDate.getDate(hitMap.get("articlePublishDate").toString()),
                        EsDate.getDate(hitMap.get("articleUpdateDate").toString()));
                esResponseList.add(esResponse);
            }
        }
        //转换分页后的数据量
        all.put("total",hits.getTotalHits());
        all.put("searchAll",esResponseList);
        return all;
    }

    //ES分类搜索
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<CategoryEsResponse> categoryHighEs(String categoryName) {
        List<CategoryEsResponse> categoryEsResponseList=new ArrayList<>();
        //全部数据查询
        SearchRequestBuilder searchRequestBuilder=transportClient.prepareSearch(categoryName.toLowerCase())
                .setTypes("article");
        //查询最新的三篇数据
        SearchResponse searchResponse=searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery())
                .setFrom(0)
                .setSize(3)
                .addSort("articleId", SortOrder.DESC)
                .execute()
                .actionGet();
        //取出结果集
        SearchHits hits=searchResponse.getHits();
        //转换成List结果集
        for(SearchHit hit:hits){
            //JSON字符串转换成实体
            CategoryEsResponse categoryEsResponse= JSONObject.parseObject(hit.getSourceAsString(), CategoryEsResponse.class);
            categoryEsResponseList.add(categoryEsResponse);
        }
        LOGGER.info(categoryEsResponseList+"");
        return categoryEsResponseList;
    }
}
