package com.article.Service;


import com.article.Entity.UserArticleRank;

import java.util.List;

public interface ArticleRedisService {

    //缓存当前用户的点赞博客的记录
    void  setCacheBlog(Long blogId,Long userId,Integer status) throws Exception;

    //获取博客的总点赞数
    Long getCacheBlogTotal(Long blogId) throws Exception;

    //触发博客点赞赞总数排行榜
    void rankBlogThump() throws Exception;

    //获得博客点赞排行榜
    List<UserArticleRank> getArticleRank() throws Exception;

    //缓存当前用户的收藏博客的记录
    void  setCacheBlogCollection(Long blogId,Long userId,Integer status) throws Exception;

    //获取博客的总收藏数
    Long getCacheBlogCollectionTotal(Long blogId) throws Exception;

}
