package com.article.Controller;


import com.article.Entity.UserArticleComment;
import com.article.Mapper.UserArticleMapper;
import com.article.Service.ArticleCommentService;
import com.article.feign.AuthorAvatarFeign;
import com.article.feign.AuthorBlogNameFeign;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CommentController {

    @Autowired
    ArticleCommentService articleCommentService;

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    AuthorBlogNameFeign authorBlogNameFeign;

    @Autowired
    AuthorAvatarFeign authorAvatarFeign;

    @Autowired
    UserArticleMapper userArticleMapper;

    //分页获取整个评论的数据
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/commentPage")
    public Map<String,Object> commentPage(@RequestParam("articleId") Long articleId,
                                          @RequestParam(name = "page", defaultValue = "1") int page,
                                          @RequestParam(name = "count", defaultValue = "10") int count){
        Map<String, Object> resultMap = new HashMap<>();
        PageInfo pageInfo=articleCommentService.getCommentPage(articleId,page,count);
        List<UserArticleComment> authorIdAll=pageInfo.getList();
        Map<String, Object> blogNameAll = new HashMap<>();
        Map<String, Object> avatarUrlAll = new HashMap<>();
        String authorIdList="";
        for(int i=0;i<authorIdAll.size();i++){
            UserArticleComment userArticleComment=authorIdAll.get(i);
            authorIdList=authorIdList.toString()+userArticleComment.getCommentatorId()+",";
        }
        LOGGER.info("你的最终列表是"+authorIdList);
        //负载均衡查询所有用户的头像和用户名
        blogNameAll=authorBlogNameFeign.getAuthorBlogNameAll(authorIdList);
        if (blogNameAll.get("success").equals("success")){
            resultMap.put("blogNameAll",blogNameAll.get("blogNameAll"));
        }
        avatarUrlAll=authorAvatarFeign.getAuthorAvatarAll(authorIdList);
        if (avatarUrlAll.get("success").equals("success")){
            resultMap.put("avatarUrlAll",avatarUrlAll.get("avatarUrlAll"));
        }
        resultMap.put("success",true);
        resultMap.put("list",pageInfo.getList());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }


    //发表一级评论
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/commentPublish")
    public Map<String,Object> commentPublish(@RequestParam("id") Long articleId, @RequestParam("comment") String comment,
                                             @RequestParam("commentatorId") Long commentatorId){
        Map<String, Object> resultMap = new HashMap<>();
        //插入一级评论并且设置父Id
        articleCommentService.publishCommentOne(articleId,comment,commentatorId);
        //增加当篇文章的评论量(不建议放到缓存，评论的占据内存多)
        userArticleMapper.updateArticleCommentById(articleId);
        resultMap.put("success",true);
        //更新文章表的评论总数
        return resultMap;
    }
}
