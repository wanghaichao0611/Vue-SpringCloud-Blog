package com.article.Mapper;

import com.article.Entity.UserArticleRank;import com.article.Entity.UserArticleThump;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserArticleThumpMapper {

    //插入用户的点赞记录
    int insert(UserArticleThump record);

    //根据博客Id跟用户Id查询点赞记录
    UserArticleThump findAllByBlogIdAndUserId(@Param("blogId") Long blogId, @Param("userId") Long userId);

    //根据博客Id查询总的点赞数
    int countByBlogId(@Param("blogId") Long blogId);

    //取消点赞博客
    int updateThumpStatusByBlogIdAndUserId(@Param("blogId") Long blogId, @Param("userId") Long userId);

    //查询博客带你赞总数排行榜
    List<UserArticleRank> getTotalRank(@Param("from") Date from, @Param("to") Date to);

    //删除表中的某个点赞记录
    int deleteByBlogIdAndUserId(@Param("blogId")Long blogId,@Param("userId")Long userId);

    //更新二次点赞微博
    int updateTwoThump(@Param("blogId") Long blogId, @Param("userId") Long userId);

}