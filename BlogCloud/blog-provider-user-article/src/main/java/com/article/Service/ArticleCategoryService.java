package com.article.Service;

import com.article.Entity.UserArticleCategory;
import org.springframework.stereotype.Service;

@Service
public interface ArticleCategoryService {

    //通过分类表的ID找到分类
    UserArticleCategory findCategoryAllById(Long categoryId);
}
