package com.feign;

import com.requset.BlogFrontPublish;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "blog-provider-user-article",fallbackFactory = UserArticleFallback.class)
public interface UserArticleFeign {

    //发表一章博客
    @RequestMapping(value = "/articlePublish", method = RequestMethod.POST)
    public Map<String,Object> articlePublish(@RequestBody BlogFrontPublish blogFrontPublish,@RequestParam("id") Long id);

    //查询自己所发表过的文章
    @RequestMapping(value = "/articlePageMine", method = RequestMethod.POST)
    public Map<String,Object> articlePageMine(@RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "count", defaultValue = "5") int count,
                                              @RequestParam("id") Long id);

    //删除自己发表过的文章
    @RequestMapping(value = "/articleDeleteOne", method = RequestMethod.POST)
    public Map<String,Object> articleDeleteOne(@RequestParam("articleId")Long articleId,@RequestParam("authorId")Long authorId);

    //我的所有博客的分页查询(防止爬虫)
    @RequestMapping(value = "/articlePageMineNext", method = RequestMethod.POST)
    public Map<String,Object> articlePageMineNext(@RequestParam("page")int page,@RequestParam("count") int count,@RequestParam("id") Long id);

    //通过文章ID查找文章
    @RequestMapping(value = "/selectAllByArticleId", method = RequestMethod.POST)
    public Map<String,Object> selectAllByArticleId(@RequestParam("articleId") Long articleId, @RequestParam("id") Long id);

    //看到所有人的博客
    @RequestMapping(value = "/articleVisitAll", method = RequestMethod.POST)
    public Map<String,Object> articleVisitAll(@RequestParam(name = "page", defaultValue = "1") int page,
                                                  @RequestParam(name = "count", defaultValue = "20") int count);

    //获取更新的标题和内容
    @RequestMapping(value = "/articleTitleMd", method = RequestMethod.POST)
    public Map<String,Object> articleTitleMd(@RequestParam("articleId") Long articleId,@RequestParam("authorId") Long authorId);

    //确认更新文章的全部信息
    @RequestMapping(value = "/articleUpdate", method = RequestMethod.POST)
    public Map<String,Object> articleUpdate(@RequestBody BlogFrontPublish blogFrontPublish,@RequestParam("authorId") Long authorId);

    //分页获取整个评论
    @RequestMapping(value = "/commentPage", method = RequestMethod.POST)
    public Map<String,Object> commentPage(@RequestParam("articleId") Long articleId,
                                          @RequestParam(name = "page", defaultValue = "1") int page,
                                          @RequestParam(name = "count", defaultValue = "10") int count);

    //发表一级评论
    @RequestMapping(value = "/commentPublish", method = RequestMethod.POST)
    public Map<String,Object> commentPublish(@RequestParam("id") Long articleId, @RequestParam("comment") String comment,
                                             @RequestParam("commentatorId") Long commentatorId);

    //点赞用户的文章
    @RequestMapping(value = "/thumpArticle", method = RequestMethod.POST)
    public Map<String,Object>  thumpArticle(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId);

    //取消点赞用户的文章
    @RequestMapping(value = "/cancelThump", method = RequestMethod.POST)
    public Map<String,Object>  cancelThump(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId);

    //查询博客的排行榜
    @RequestMapping(value = "/thumpRank", method = RequestMethod.POST)
    public Map<String,Object>  thumpRank();

    //收藏用户的文章
    @RequestMapping(value = "/collectArticle", method = RequestMethod.POST)
    public Map<String,Object>  collectArticle(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId);

    //取消收藏用户的文章
    @RequestMapping(value = "/cancelCollect", method = RequestMethod.POST)
    public Map<String,Object>  cancelCollect(@RequestParam("blogId")Long blogId,@RequestParam("userId")Long userId);

    //查询自己所收藏过的文章
    @RequestMapping(value = "/articleCollection", method = RequestMethod.POST)
    public Map<String,Object> articleCollection(@RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "count", defaultValue = "5") int count,
                                              @RequestParam("id") Long id);

    //查询草稿箱的文章
    @RequestMapping(value = "/selectDraft", method = RequestMethod.POST)
    public Map<String,Object>  selectDraft(@RequestParam("id") Long id);

    //保存草稿箱的文章
    @RequestMapping(value = "/insertDraft", method = RequestMethod.POST)
    public Map<String,Object>  insertDraft(@RequestParam("articleTitle")String articleTitle,@RequestParam("articleContent")String articleContent,
                                           @RequestParam("id") Long id);
}
@Component
class UserArticleFallback implements FallbackFactory<UserArticleFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserArticleFallback.class);

    @Override
    public UserArticleFeign create(Throwable throwable) {
        return new UserArticleFeign() {

            //发表文章的fallback
            @Override
            public Map<String, Object> articlePublish(BlogFrontPublish blogFrontPublish,Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "创建超时!");
                return feignMap;
            }

            //分页查询自己发表过的博客的fallback
            @Override
            public Map<String, Object> articlePageMine(int page, int count,Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //删除自己发表过的文章的fallback
            @Override
            public Map<String, Object> articleDeleteOne(Long articleId, Long authorId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "删除超时!");
                return feignMap;
            }

            //我的所有博客的分页查询的fallback
            @Override
            public Map<String, Object> articlePageMineNext(int page, int count,Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "分页查询超时!");
                return feignMap;
            }

            //通过文章ID查找前端需要的信息的fallback
            @Override
            public Map<String, Object> selectAllByArticleId(Long articleId, Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查找超时!");
                return feignMap;
            }

            //看到所有人的博客的fallback
            @Override
            public Map<String, Object> articleVisitAll(int page, int count) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //获取更新的标题和内容的fallback
            @Override
            public Map<String, Object> articleTitleMd(Long articleId, Long authorId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //确认更新文章的全部信息的fallback
            @Override
            public Map<String, Object> articleUpdate(BlogFrontPublish blogFrontPublish, Long authorId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时!");
                return feignMap;
            }

            //分页获取整个评论
            @Override
            public Map<String, Object> commentPage(Long articleId, int page, int count) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "分页获取超时!");
                return feignMap;
            }

            //发表一级评论
            @Override
            public Map<String, Object> commentPublish(Long articleId, String comment, Long commentatorId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "评论超时!");
                return feignMap;
            }

            //点赞用户的评论
            @Override
            public Map<String, Object> thumpArticle(Long blogId, Long userId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "点赞超时!");
                return feignMap;
            }

            //取消点赞用户的文章
            @Override
            public Map<String, Object> cancelThump(Long blogId, Long userId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "取消点赞超时!");
                return feignMap;
            }

            //查询博客的排行榜
            @Override
            public Map<String, Object> thumpRank() {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询排行榜超时!");
                return feignMap;
            }

            //收藏用户的文章
            @Override
            public Map<String, Object> collectArticle(Long blogId, Long userId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "收藏超时!");
                return feignMap;
            }

            //取消收藏用户的文章
            @Override
            public Map<String, Object> cancelCollect(Long blogId, Long userId) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "取消收藏超时!");
                return feignMap;
            }

            //查询自己所收藏过的文章
            @Override
            public Map<String, Object> articleCollection(int page, int count, Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询收藏超时!");
                return feignMap;
            }

            //查询草稿箱的文章
            @Override
            public Map<String, Object> selectDraft(Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询草稿超时!");
                return feignMap;
            }

            //保存草稿箱的文章
            @Override
            public Map<String, Object> insertDraft(String articleTitle,String articleContent,Long id) {
                UserArticleFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "保存草稿超时!");
                return feignMap;
            }
        };
    }
}
