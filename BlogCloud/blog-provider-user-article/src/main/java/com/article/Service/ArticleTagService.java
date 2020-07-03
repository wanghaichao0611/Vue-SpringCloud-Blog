package com.article.Service;

import com.article.Entity.UserArticleTag;
import org.springframework.stereotype.Service;


@Service
public interface ArticleTagService {

    //通过文章标签的ID找到所属文章的标签
    UserArticleTag findTagAllById(String tagId);

}
