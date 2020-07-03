package com.article.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.article.Entity.UserArticleCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserArticleCategoryMapper {

    //插入文章分类表的信息
    int insert(UserArticleCategory record);

    //查询是否存在这个分类的名字
    UserArticleCategory findAllByCategoryName(@Param("categoryName")String categoryName);

    //更新分类表中关联的文章的ID
    int updateCategoryNameAndCategoryArticlesById(@Param("updatedCategoryName")String updatedCategoryName,@Param("updatedCategoryArticles")String updatedCategoryArticles,@Param("id")Long id);

    //通过分类表的ID找到分类的名字
    UserArticleCategory selectAllById(@Param("id")Long id);

    //更新分类表中的文章Id
    int updateCategoryArticlesById(@Param("updatedCategoryArticles")String updatedCategoryArticles,@Param("id")Long id);

}