package com.article.Service;

import com.article.Entity.UserArticleRank;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface ArticleThumpService {

    //Redis分布式锁—点赞博客
    int thumpLock(Long blogId,Long userId) throws Exception;

    //取消点赞博客
    void cancelThump(Long blogId,Long userId) throws Exception;

    //获取博客的点赞总量
    Long getArticleThumpTotal(Long blogId,Long userId) throws Exception;

    //获取是否完成点赞
    int getThumpButton(Long blogId,Long userId);

    //获取博客带你赞总数排行榜
    Collection<UserArticleRank> getArticleRank() throws Exception;

}
