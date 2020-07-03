package com.article.Service;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface ArticleCollectionService {

    //Redis分布式锁—收藏博客
    int collectLock(Long blogId,Long userId) throws Exception;

    //取消收藏博客
    void cancelCollect(Long blogId,Long userId) throws Exception;

    //获取是否完成收藏
    int getCollectButton(Long blogId,Long userId);

    //获取博客的收藏总量
    Long getArticleCollectionTotal(Long blogId,Long userId) throws Exception;

    //根据userId分页获取所有收藏记录
    PageInfo getAllCollection(int page, int count, Long userId);
}
