package com.article.Controller;

import com.article.Entity.UserArticle;
import com.article.Entity.UserArticleRank;
import com.article.Service.ArticleService;
import com.article.Service.ArticleThumpService;
import com.article.feign.ArticleMsgFeign;
import com.article.feign.ArticleSignFeign;
import com.article.response.ArticleRank;
import com.article.feign.AuthorBlogNameFeign;
import com.article.util.DateTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.*;

@RestController
public class ThumpController {

    //开启日志观察
    private static final Logger LOGGER= LoggerFactory.getLogger(ThumpController.class);

    //文章的服务
    @Autowired
    ArticleService articleService;

    //点赞的服务
    @Autowired
    ArticleThumpService articleThumpService;

    //负载均衡查博客名
    @Autowired
    AuthorBlogNameFeign authorBlogNameFeign;

    //负载均衡增加经验值
    @Autowired
    ArticleSignFeign articleSignFeign;

    //负载均衡告知点赞信息
    @Autowired
    ArticleMsgFeign articleMsgFeign;

    //点赞用户的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/thumpArticle")
    public Map<String,Object>  thumpArticle(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            //调用分布式加锁方式
            int result=articleThumpService.thumpLock(blogId, userId);
            //获取当前博客的总赞数
            Long total=articleThumpService.getArticleThumpTotal(blogId, userId);
            if (result>0){
                //总点赞数保存到文章中
                articleService.setThumpTotal(blogId,total.intValue());
            }
            boolean flag=result>0;
            resultMap.put("success",flag);
            resultMap.put("thumpTotal",total);
        }catch (Exception e){
           LOGGER.info("点赞博客发生异常了:",e.fillInStackTrace());
        }
        return resultMap;
    }

    //取消点赞博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/cancelThump")
    public Map<String,Object>  cancelThump(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            //调用取消点赞的处理方法
            articleThumpService.cancelThump(blogId, userId);
            //获取当前博客的总赞数
            Long total=articleThumpService.getArticleThumpTotal(blogId, userId);
            resultMap.put("success",true);
            resultMap.put("thumpTotal",total);
            //总点赞数保存到文章中
            articleService.setThumpTotal(blogId,total.intValue());
        }catch (Exception e){
            LOGGER.info("取消点赞博客发生了异常:",e.fillInStackTrace());
        }
        return resultMap;
    }

    //获取博客排行榜
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/thumpRank")
    public Map<String,Object>  thumpRank(){
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> blogNameAll = new HashMap<>();
        try {
            //排行榜中的Id列表(只有10个Id所以不大)
            List<Long> idList=new ArrayList<>();
            //负载均衡用到List<String>
            String authorStringList="";
            //文章的部分List信息(交换的临时)
            List<ArticleRank> rankList=new ArrayList<>();
            //文章的全部List信息(最后的结果)
            List<ArticleRank> rankListEnd=new ArrayList<>();
            //Collection集合
            Collection<UserArticleRank> collectionRank=articleThumpService.getArticleRank();
            //获取缓存中排行榜的Id集合
            List<UserArticleRank> userArticleRanks=new ArrayList<UserArticleRank>(collectionRank);
            //转换排行榜中的Id
            for (int i=0;i<userArticleRanks.size();i++){
                UserArticleRank userArticleRank=userArticleRanks.get(i);
                Long id=userArticleRank.getBlogId().longValue();
                idList.add(id);
            }
            //添加文章的部分信息
            for (int i=0;i<idList.size();i++){
                Long id=idList.get(i);
                ArticleRank rank=articleService.getArticleRank(id);
                rankList.add(rank);
                authorStringList=authorStringList.toString()+rank.getAuthorId()+",";
            }
            //负载均衡博客名
            blogNameAll=authorBlogNameFeign.getAuthorBlogNameAll(authorStringList);
            if (blogNameAll.get("success").equals("success")){
                resultMap.put("blogNameAll",blogNameAll.get("blogNameAll"));
            }
            //整合一个List
            for (int i=0;i<rankList.size();i++){
                ArticleRank articleRank=rankList.get(i);
                articleRank.setTotal(userArticleRanks.get(i).getTotal());
                //拆分用户的博客名
                Object nameAll=blogNameAll.get("blogNameAll");
                String[] a=nameAll.toString().split(",");
                articleRank.setName(a[i]);
                //完成整合后的List交换
                rankListEnd.add(articleRank);
            }

            LOGGER.info("最初的数据"+collectionRank);
            LOGGER.info("排行榜"+userArticleRanks);
            LOGGER.info("排行榜的Id列表"+idList+authorStringList);
            LOGGER.info("负载均衡"+blogNameAll.get("blogNameAll"));
            LOGGER.info("测试"+rankList);
            LOGGER.info("最后"+rankListEnd);
            resultMap.put("list",rankListEnd);
        }catch (Exception e){
            LOGGER.info("获取博客点赞排行榜-发生异常: ",e.fillInStackTrace());
        }
        return resultMap;
    }
}
