package com.article.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.article.Entity.UserArticleTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserArticleTagMapper {

    //插入文章标签表的信息
    int insert(UserArticleTag record);

    //通过标签名查询是否存在这个标签
    UserArticleTag findAllByTagName(@Param("tagName")String tagName);

    //更新文章标签的ID
    int updateTagNameAndTagArticlesById(@Param("updatedTagName")String updatedTagName,@Param("updatedTagArticles")String updatedTagArticles,@Param("id")Long id);

    //通过标签ID查找所有的信息
    UserArticleTag selectAllById(@Param("id")Long id);

    //通过标签ID更新文章ID集合
    int updateTagArticlesById(@Param("updatedTagArticles")String updatedTagArticles,@Param("id")Long id);


}