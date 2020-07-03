package com.article.Mapper;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.article.Entity.UserArticleCollection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserArticleCollectionMapper {

    //插入收藏信息
    int insert(UserArticleCollection record);

    //根据博客Id和用户Id查询用户的收藏记录
    UserArticleCollection findAllByBlogIdAndUserId(@Param("blogId")Long blogId,@Param("userId")Long userId);

    //更新二次收藏微博
    int updateTwoCollection(@Param("blogId") Long blogId, @Param("userId") Long userId);

    //取消收藏博客
    int updateCollectStatusByBlogIdAndUserId(@Param("blogId") Long blogId, @Param("userId") Long userId);

    //根据userId查询所有的博客的Id，降序收藏时间和成功收藏地文章。
    List<Long> findBlogIdByUserId(@Param("userId")Long userId);






}