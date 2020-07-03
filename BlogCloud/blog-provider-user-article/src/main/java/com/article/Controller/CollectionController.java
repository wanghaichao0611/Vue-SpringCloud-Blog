package com.article.Controller;

import com.article.Entity.UserArticle;
import com.article.Service.ArticleCollectionService;
import com.article.Service.ArticleService;
import com.article.response.ArticleCollection;
import com.article.feign.AuthorBlogNameFeign;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CollectionController {

    //开启日志观察
    private static final Logger LOGGER= LoggerFactory.getLogger(ThumpController.class);

    //文章的服务
    @Autowired
    ArticleService articleService;

    //收藏的服务
    @Autowired
    ArticleCollectionService articleCollectionService;

    //用户的博客名的服务
    @Autowired
    AuthorBlogNameFeign authorBlogNameFeign;

    //收藏用户的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/collectArticle")
    public Map<String,Object> collectArticle(@RequestParam("blogId")Long blogId, @RequestParam("userId")Long userId){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //调用分布式加锁方式
            int result=articleCollectionService.collectLock(blogId, userId);
            //获取当前博客的总收藏数
            Long total=articleCollectionService.getArticleCollectionTotal(blogId, userId);
            boolean flag=result>0;
            resultMap.put("success",flag);
            resultMap.put("collectTotal",total);
            //总收藏数保存到文章中
            articleService.setCollectionTotal(blogId,total.intValue());
        }catch (Exception e){
            LOGGER.info("收藏博客发生异常了：",e.fillInStackTrace());
        }
        return resultMap;
    }

    //取消收藏博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/cancelCollect")
    public Map<String,Object>  cancelCollect(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            //调用取消收藏的处理方法
            articleCollectionService.cancelCollect(blogId, userId);
            //获取当前博客的总收藏数
            Long total=articleCollectionService.getArticleCollectionTotal(blogId, userId);
            resultMap.put("success",true);
            resultMap.put("collectTotal",total);
            //总收藏数保存到文章中
            articleService.setCollectionTotal(blogId,total.intValue());
        }catch (Exception e){
            LOGGER.info("取消点赞博客发生了异常：",e.fillInStackTrace());
        }
        return resultMap;
    }

    //分页获取自己所收藏过的博客(前端)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleCollection")
    public Map<String,Object> articleCollection(@RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "count", defaultValue = "5") int count,
                                              @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> blogNameAll = new HashMap<>();
       //分页获取本人收藏表中的所有记录
        PageInfo pageInfo=articleCollectionService.getAllCollection(page, count, id);
        //转换分页后的List博客Id
        List<Long> blogIdList=pageInfo.getList();
        //生成文章的实体List类型
        List<UserArticle> userArticleList=new ArrayList<>();
        //生成前端收藏文章的最终List实体
        List<ArticleCollection> articleCollections=new ArrayList<>();
        //生成String的文章Id
        String idStringList="";
        //取出List中的文章Id,生成完整的文章List
        for (int i=0;i<blogIdList.size();i++){
            //生成博客Id
            Long blogId=blogIdList.get(i);
            //根据博客Id查询所有的信息，也可以不查询所有自己定义Type或者Map。
            userArticleList.add(articleService.findArticleAllById(blogId));
            //生成负载均衡用户名的idStingList
            idStringList=idStringList.toString()+userArticleList.get(i).getArticleAuthorId()+",";
        }
        //负载均衡用户名
        blogNameAll=authorBlogNameFeign.getAuthorBlogNameAll(idStringList);
        if (blogNameAll.get("success").equals("success")){
            resultMap.put("blogNameAll",blogNameAll.get("blogNameAll"));
        }
        //整合一个完整的List返还给前端
        for (int i=0;i<userArticleList.size();i++){
            //整合Response的List
            UserArticle userArticle=userArticleList.get(i);
            ArticleCollection articleCollection=new ArticleCollection();
            articleCollection.setId(userArticle.getId());
            articleCollection.setTitle(userArticle.getArticleTitle());
            articleCollection.setArticlePublishTime(userArticle.getArticlePublishDate());
            articleCollection.setArticleUpdateTime(userArticle.getArticleUpdateDate());
            articleCollection.setTotal(userArticle.getArticleCollection());
            //拆分用户的博客名
            Object nameAll=blogNameAll.get("blogNameAll");
            String[] a=nameAll.toString().split(",");
            articleCollection.setName(a[i]);
            //完成List的整合
            articleCollections.add(articleCollection);
        }
        resultMap.put("success",true);
        resultMap.put("list",articleCollections);
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }
}
