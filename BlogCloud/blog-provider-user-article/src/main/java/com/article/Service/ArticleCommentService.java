package com.article.Service;


import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface ArticleCommentService {

    //分页获取整个评论
    PageInfo getCommentPage(Long articleId,int page,int count);

    //发表一级评论
    void publishCommentOne( Long articleId,String comment,Long commentatorId);
}
