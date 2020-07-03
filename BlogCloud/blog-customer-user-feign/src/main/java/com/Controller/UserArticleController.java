package com.Controller;

import com.Service.UsernameToken;
import com.feign.UserArticleFeign;
import com.mapper.UserMapperToken;
import com.requset.BlogFrontPublish;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserArticleController {

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;

    //Article的Feign
    @Autowired
    private UserArticleFeign userArticleFeign;

    //发布个人的文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articlePublishFeign")
    @AuthToken
    public Map<String,Object> articlePublishFeign(BlogFrontPublish blogFrontPublish){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.articlePublish(blogFrontPublish,id);
    }

    //查询自己所有发表过的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articlePageMineFeign")
    @AuthToken
    public Map<String,Object> articlePageMineFeign(@RequestParam(name = "page", defaultValue = "1") int page,
                                                   @RequestParam(name = "count", defaultValue = "5") int count){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.articlePageMine(page,count,id);
    }

    //删除自己发表过的文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleDeleteOneFeign")
    public Map<String,Object> articleDeleteOneFeign(@RequestParam("articleId")Long articleId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long authorId=userMapperToken.getId(username);
        return userArticleFeign.articleDeleteOne(articleId,authorId);
    }


    //所有博客的分页查询(防止爬虫未使用)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articlePageMineNextFeign")
    @AuthToken
    public Map<String,Object> articlePageMineNextFeign(@RequestParam("page")String page,@RequestParam("count") String count){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.articlePageMineNext(Integer.parseInt(page),Integer.parseInt(count),id);
    }

    //通过文章ID查找前端需要的信息，文章ID与作者ID一致才显示编辑按钮。
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectAllByArticleIdFeign")
    @AuthToken
    public Map<String,Object> selectAllByArticleIdFeign(@RequestParam("articleId") Long articleId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.selectAllByArticleId(articleId,id);

    }

    //游客查询文章的所有的关联
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleVisitAllFeign")
    @AuthToken
    public Map<String,Object> articleVisitAllFeign(@RequestParam(name = "page", defaultValue = "1") int page,
                                                   @RequestParam(name = "count", defaultValue = "20") int count){
        return userArticleFeign.articleVisitAll(page, count);
    }

    //更新文章先获取文章的标题和内容
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleTitleMdFeign")
    @AuthToken
    public Map<String,Object> articleTitleMdFeign(@RequestParam("articleId") Long articleId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long authorId=userMapperToken.getId(username);
        return userArticleFeign.articleTitleMd(articleId,authorId);
    }

    //确认修改文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleUpdateFeign")
    @AuthToken
    public Map<String,Object> articleUpdateFeign(BlogFrontPublish blogFrontPublish){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long authorId=userMapperToken.getId(username);
        return userArticleFeign.articleUpdate(blogFrontPublish,authorId);
    }

    //分页获取整个评论的数据
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/commentPageFeign")
    @AuthToken
    public Map<String,Object> commentPageFeign(@RequestParam("articleId") Long articleId,
                                                  @RequestParam(name = "page", defaultValue = "1") int page,
                                                  @RequestParam(name = "count", defaultValue = "10") int count){
        return userArticleFeign.commentPage(articleId,page,count);
    }
    //发表一级评论
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/commentPublishFeign")
    @AuthToken
    public Map<String,Object> commentPublishFeign(@RequestParam("id") Long articleId,@RequestParam("comment") String comment){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long commentatorId=userMapperToken.getId(username);
        return userArticleFeign.commentPublish(articleId,comment,commentatorId);
    }

    //点赞用户的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/thumpArticleFeign")
    @AuthToken
    public Map<String,Object>  thumpArticleFeign(@RequestParam("articleId")Long blogId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long userId=userMapperToken.getId(username);
        return userArticleFeign.thumpArticle(blogId,userId);
    }

    //取消点赞用户的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/cancelThumpFeign")
    @AuthToken
    public Map<String,Object>  cancelThumpFeign(@RequestParam("articleId")Long blogId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long userId=userMapperToken.getId(username);
        return userArticleFeign.cancelThump(blogId,userId);
    }

    //查询博客排行榜(无Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/thumpRankFeign")
    public Map<String,Object>  thumpRankFeign(){
        return userArticleFeign.thumpRank();
    }

    //收藏用户的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/collectArticleFeign")
    @AuthToken
    public Map<String,Object>  collectArticleFeign(@RequestParam("articleId")Long blogId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long userId=userMapperToken.getId(username);
        return userArticleFeign.collectArticle(blogId,userId);
    }

    //取消收藏用户的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/cancelCollectFeign")
    @AuthToken
    public Map<String,Object>  cancelCollectFeign(@RequestParam("articleId")Long blogId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long userId=userMapperToken.getId(username);
        return userArticleFeign.cancelCollect(blogId,userId);
    }

    //查询自己所收藏过的博客
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/articleCollectionFeign")
    @AuthToken
    public Map<String,Object> articleCollectionFeign(@RequestParam(name = "page", defaultValue = "1") int page,
                                                   @RequestParam(name = "count", defaultValue = "5") int count){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.articleCollection(page,count,id);
    }

    //查询草稿箱的文章
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectDraftFeign")
    @AuthToken
    public Map<String,Object>  selectDraftFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.selectDraft(id);
    }

    //保存草稿箱的内容(若是存在则是二次保存草稿)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/insertDraftFeign")
    @AuthToken
    public Map<String,Object>  insertDraftFeign(@RequestParam("articleTitle")String articleTitle,@RequestParam("articleContent")String articleContent){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userArticleFeign.insertDraft(articleTitle,articleContent,id);
    }
}
