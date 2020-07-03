package com.article.Controller;

import com.article.Entity.UserArticle;
import com.article.Entity.UserArticleCategory;
import com.article.Entity.UserArticleDraft;
import com.article.Entity.UserArticleTag;
import com.article.Mapper.UserArticleDraftMapper;
import com.article.Mapper.UserArticleMapper;
import com.article.Service.*;
import com.article.response.BlogFrontPublish;
import com.article.response.UserArticleCountAll;
import com.article.util.DateTo;
import com.article.feign.AuthorAvatarFeign;
import com.article.feign.AuthorBlogNameFeign;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ArticleController {

    //文章的服务
    @Autowired
    ArticleService articleService;

    //点赞的服务
    @Autowired
    ArticleThumpService articleThumpService;

    //收藏的服务
    @Autowired
    ArticleCollectionService articleCollectionService;

    //分类的服务
    @Autowired
    ArticleCategoryService articleCategoryService;

    //标签的服务
    @Autowired
    ArticleTagService articleTagService;

    //用户的博客名的服务
    @Autowired
    AuthorBlogNameFeign authorBlogNameFeign;

    //用户头像的服务
    @Autowired
    AuthorAvatarFeign authorAvatarFeign;

    //草稿箱的服务
    @Autowired
    UserArticleDraftMapper userArticleDraftMapper;

    //懒省事的服务
    @Autowired
    UserArticleMapper userArticleMapper;

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleController.class);

    //发表一章博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articlePublish")
    public Map<String,Object> articlePublish(@RequestBody BlogFrontPublish blogFrontPublish,@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //判断放到哪个分类当中
        articleService.articlePublish(blogFrontPublish,id);
        //删除草稿箱中的文章
        userArticleDraftMapper.deleteByUserId(id);
        resultMap.put("success",true);
        resultMap.put("message","发布文章成功!");
        return resultMap;
    }

    //分页获取自己所发表过的博客(前端)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articlePageMine")
    public Map<String,Object> articlePageMine(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "count", defaultValue = "5") int count,
                                          @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //获得本人所有发表过的博客
        PageInfo pageInfo=articleService.articlePage(page,count,id);
        resultMap.put("success",true);
        resultMap.put("list",pageInfo.getList());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    //删除自己发表过的文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleDeleteOne")
    public Map<String,Object> articleDeleteOne(@RequestParam("articleId")Long articleId,@RequestParam("authorId")Long authorId){
        Map<String, Object> resultMap = new HashMap<>();
        //删除自己发表过的文章
        int result=articleService.articleDeleteOne(articleId,authorId);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "博客删除成功!":"非法访问,拒绝服务!");
        return resultMap;
    }

    //分页下一页查询所有发表的博客(防止爬虫并未使用)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articlePageMineNext")
    public Map<String,Object> articlePageMineNext(@RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "count", defaultValue = "10") int count,
                                              @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //获得本人所有发表过的博客
        PageInfo pageInfo=articleService.articlePage(page,count,id);
        resultMap.put("success",true);
        resultMap.put("Mine",pageInfo);
        return resultMap;
    }

    //通过文章ID查找前端需要的信息，文章ID与作者ID一致才显示编辑按钮。
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectAllByArticleId")
    public Map<String,Object> selectAllByArticleId(@RequestParam("articleId") Long articleId,@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> blogNameAll = new HashMap<>();
        Map<String, Object> avatarUrlAll = new HashMap<>();
        //先确定是本人操作还是他人浏览，他人操作的话访问量需要+1。(必须审核通过的文章才可以被他人浏览)
        int result=articleService.checkAdmin(articleId,id);
        if (result==1){
            //获取点赞的按钮
            int thumpButton=articleThumpService.getThumpButton(articleId,id);
            if (thumpButton>0){
                resultMap.put("thumpButton",true);
            }else {
                resultMap.put("thumpButton", false);
            }
            //获取收藏的按钮
            int collectButton=articleCollectionService.getCollectButton(articleId,id);
            if (collectButton>0){
                resultMap.put("collectButton",true);
            }else {
                resultMap.put("collectButton", false);
            }
            //返还给前端需要的所有信息
            UserArticle userArticle=articleService.findArticleAllById(articleId);
            UserArticleCategory userArticleCategory=articleCategoryService.findCategoryAllById(userArticle.getArticleCategoryId());
            UserArticleTag userArticleTag=articleTagService.findTagAllById(userArticle.getArticleTagsId());
            List<UserArticle> userArticleList=articleService.findArticleListAllById(articleId);
            UserArticle userArticleTo=userArticleList.get(0);//多次一举，不想再改动了
            //返还给前端指定文章的数据，分类和标签。
            resultMap.put("success",true);
            resultMap.put("articleAll",userArticleList);
            resultMap.put("articleCategoryName",userArticleCategory.getCategoryName());
            resultMap.put("articleTag",userArticleTag.getTagName());
            //负载均衡查询当前用户的头像和用户名
            blogNameAll=authorBlogNameFeign.getAuthorBlogNameAll(userArticleTo.getArticleAuthorId().toString());
            if (blogNameAll.get("success").equals("success")){
                resultMap.put("authorName",blogNameAll.get("blogNameAll"));
            }
            avatarUrlAll=authorAvatarFeign.getAuthorAvatarAll(userArticleTo.getArticleAuthorId().toString());
            if (avatarUrlAll.get("success").equals("success")){
                resultMap.put("authorAvatar",avatarUrlAll.get("avatarUrlAll"));
            }
            if (userArticle.getArticleAuthorId().equals(id)){
                resultMap.put("myself",true);
            }
            //获取当前用户的各个文章数据集合
            resultMap.put("userArticleAll",userArticleMapper.selectUserAllById(userArticleTo.getArticleAuthorId()));
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("message","您未有权限,非法访问!");
            resultMap.put("myself",false);
            return resultMap;
        }
    }

    //文章主页查看他人发表的文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleVisitAll")
    public Map<String,Object> articleVisitAll(@RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "count", defaultValue = "20") int count){
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> blogNameAll = new HashMap<>();
        Map<String, Object> avatarUrlAll = new HashMap<>();
        String authorIdList="";
        //评论最后再完善,查询公开且不是Vip的最新发表人的文章的作者ID和文章的全部信息(当天)

        //获取当前系统时间和从0点开始的时间
        //Date from=DateTo.getTimesMorning();

        //获取这个时间的上一年用于测试
        Date from=DateTo.lastYear();
        //获取当前时间点
        Date to=new Date();
        LOGGER.info("两个时间之差"+from+to);

        //分页获取这个时间段的所有的博客(审核通过的文章)
        PageInfo userArticleAll=articleService.findVisitAll(page,count,from,to);
        List<Long> authorIdAll=articleService.articleAuthorAll(from,to);
        LOGGER.info("你的Id列表是"+authorIdAll);
        for(int i=0;i<authorIdAll.size();i++){
            Long authorId=authorIdAll.get(i);
            authorIdList=authorIdList.toString()+authorId+",";
        }
        LOGGER.info("你的最终列表是"+authorIdList);
        /**
         * 查询所有符合时间段条件的博客
        List<UserArticle> authorAll=articleService.articleAllPageId(from,to);
        //查询符合AuthorID的集合
        for (int i = 0;i<authorAll.size();i++){
           UserArticle userArticle=authorAll.get(i);
           Long authorId=userArticle.getArticleAuthorId();
           authorIdList=authorId+",";
        }**/

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
        resultMap.put("articleAll",userArticleAll);
        return resultMap;
    }

    //更新文章先获取文章的标题和内容
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleTitleMd")
    public Map<String,Object> articleTitleMd(@RequestParam("articleId") Long articleId,@RequestParam("authorId") Long authorId){
        Map<String, Object> resultMap = new HashMap<>();
        //验证数据库中的作者Id与用户表的Id是否一致
        int result=articleService.checkId(articleId,authorId);
        boolean flag=result>0;
        resultMap.put("success",flag);
        if (result>0) {
            //获取文章的标题和内容
            UserArticle userArticle=articleService.findArticleAllById(articleId);
            resultMap.put("id",userArticle.getId());
            resultMap.put("articleTitle",userArticle.getArticleTitle());
            resultMap.put("articleMdContent",userArticle.getArticleMdContent());
        }
        return resultMap;
    }

    //确认更新文章的全部信息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleUpdate")
    public Map<String,Object> articleUpdate(@RequestBody BlogFrontPublish blogFrontPublish,@RequestParam("authorId") Long authorId){
        Map<String, Object> resultMap = new HashMap<>();
        //验证数据库中的作者Id与用户表的Id是否一致
        int result=articleService.checkId(blogFrontPublish.getId(),authorId);
        boolean flag=result>0;
        resultMap.put("success",flag);
        if (result>0){
            //更新文章的全部
            articleService.articleUpdate(blogFrontPublish,authorId);
        }
        return resultMap;
    }

    //查询草稿箱的文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectDraft")
    public Map<String,Object>  selectDraft(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //查询草稿箱中的文章
        UserArticleDraft userArticleDraft=userArticleDraftMapper.selectAllByUserId(id);
        resultMap.put("success",true);
        if (userArticleDraft==null){
            resultMap.put("articleTitle","");
            resultMap.put("articleContent","");
        }else {
            resultMap.put("articleTitle", userArticleDraft.getDraftTitle());
            resultMap.put("articleContent", userArticleDraft.getDraftContent());
        }
        return resultMap;
    }

    //保存草稿箱的文章(存在二次更新)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/insertDraft")
    public Map<String,Object>  insertDraft(@RequestParam("articleTitle")String articleTitle,@RequestParam("articleContent")String articleContent,
                                           @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //保存草稿箱的文章
        //先查询是否存在草稿，不存在则直接插入，存在则直接根据查出来的用户Id更新
        UserArticleDraft userArticleDraft=userArticleDraftMapper.selectAllByUserId(id);
        if (userArticleDraft==null){
            //则直接插入文章
            UserArticleDraft userArticleDraft1=new UserArticleDraft();
            userArticleDraft1.setUserId(id);
            userArticleDraft1.setDraftTitle(articleTitle);
            userArticleDraft1.setDraftContent(articleContent);
            userArticleDraft1.setDraftTime(new Date());
            userArticleDraftMapper.insert(userArticleDraft1);
        }else {
            //根据主键Id更新草稿
            userArticleDraftMapper.updateDraftById(articleTitle,articleContent,new Date(),userArticleDraft.getId());
        }
        resultMap.put("articleTitle",articleTitle);
        resultMap.put("articleContent",articleContent);
        resultMap.put("success",true);
        return resultMap;
    }
}
