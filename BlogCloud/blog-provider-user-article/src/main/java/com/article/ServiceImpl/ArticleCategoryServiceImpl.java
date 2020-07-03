package com.article.ServiceImpl;

import com.article.Entity.UserArticleCategory;
import com.article.Mapper.UserArticleCategoryMapper;
import com.article.Service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleCategoryServiceImpl implements ArticleCategoryService {

    @Autowired
    UserArticleCategoryMapper userArticleCategoryMapper;


    //通过分类表的ID找到分类
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserArticleCategory findCategoryAllById(Long categoryId) {
        UserArticleCategory userArticleCategory=userArticleCategoryMapper.selectAllById(categoryId);
        return userArticleCategory;
    }
}
