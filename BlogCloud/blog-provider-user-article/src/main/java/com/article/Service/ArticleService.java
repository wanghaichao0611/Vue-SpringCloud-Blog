package com.article.Service;

import com.article.Entity.UserArticle;
import com.article.response.ArticleRank;
import com.article.response.BlogFrontPublish;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ArticleService {

    //发表一章博客
    void articlePublish(BlogFrontPublish blogFrontPublish, Long id);

    //获取本人所有的博客
    PageInfo articlePage(int page, int count, Long id);

    //鉴定文章ID与作者ID是否一致，不一致的话再判断是否公开，私密的话则返回终止。
    //私密的文章不会传给前端显示，但是作者本人还要修改，所以防止手动篡改，所以加上整个保个险。
    //若文章ID与作者ID保持一致，则前端允许显示编辑的按钮(无法阅读审核中的文章，需要等审核通过)
    int checkAdmin(Long articleId,Long id);

    //通过文章ID查找文章的信息
    UserArticle findArticleAllById(Long articleId);

    //删除本人的文章
    int articleDeleteOne(Long articleId,Long authorId);

    //通过文章ID查找List文章的信息(List)
    List<UserArticle> findArticleListAllById(Long articleId);

    //分页获取这个时间段的作者的文章
    PageInfo findVisitAll(int page,int count,Date from,Date to);

    //查询时间差的AuthorID
    List<Long> articleAuthorAll(Date from,Date to);

    //查询时间差的所有的信息(未使用)
    List<UserArticle> articleAllPageId(Date from,Date to);

    //验证数据库中的作者Id与用户表的Id是否一致
    int checkId(Long articleId,Long authorId);

    //更新文章的全部信息
    void articleUpdate(BlogFrontPublish blogFrontPublish,Long authorId);

    //保存总点赞量到指定文章中
    void setThumpTotal(Long blogId,Integer total);

    //查询排行榜的指定信息
     ArticleRank getArticleRank(Long id);

    //保存总收藏量到指定文章中
    void setCollectionTotal(Long blogId,Integer total);

}
