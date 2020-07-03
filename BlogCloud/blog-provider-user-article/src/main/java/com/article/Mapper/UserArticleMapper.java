package com.article.Mapper;

import com.article.Entity.UserArticle;
import com.article.response.ArticleRank;
import com.article.response.UserArticleCountAll;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.Date;import java.util.List;

@Mapper
public interface UserArticleMapper {
    int insert(UserArticle record);

    //更新文章表的信息
    int updateArticleTagsIdByArticleId(@Param("updatedArticleTagsId") String updatedArticleTagsId, @Param("articleId") Long articleId);

    //查询所有的博客通过作者的ID
    List<UserArticle> selectAllByArticleAuthorId(@Param("articleAuthorId") Long articleAuthorId);

    //通过文章ID查找文章所有的信息
    UserArticle selectAllById(@Param("id") Long id);

    //通过文章ID查找文章List所有的信息
    List<UserArticle> selectListAllById(@Param("id") Long id);

    //文章的访问量+1
    int updateArticleReadingById(@Param("id") Long id);

    //发表过时间段之差最新的博客
    List<UserArticle> findAll(@Param("from") Date from, @Param("to") Date to);

    //发表博客时间之差的AuthorId的集合
    List<Long> selectArticleAuthorId(@Param("from") Date from, @Param("to") Date to);

    //通过文章Id查找作者Id
    Long selectArticleAuthorIdById(@Param("id") Long id);

    //通过文章Id查找分类Id
    Long selectArticleCategoryIdById(@Param("id") Long id);

    //通过文章Id查找标签Id
    String selectArticleTagsIdById(@Param("id") Long id);

    //删除指定的整篇文章
    int deleteById(@Param("id") Long id);

    //更新一部分信息
    int updateSome(UserArticle record);

    //通过ID更新分类表的ID
    int updateArticleCategoryIdById(@Param("updatedArticleCategoryId") Long updatedArticleCategoryId, @Param("id") Long id);

    //根据文章Id保存总点赞量
    int updateArticleThumbUpById(@Param("updatedArticleThumbUp") Integer updatedArticleThumbUp, @Param("id") Long id);

    //根据主键Id查找排行榜适合的
    ArticleRank findAllById(@Param("id") Long id);

    //根据文章Id保存总收藏量
    int updateArticleCollectionUpById(@Param("updatedArticleCollection") Integer updatedArticleCollection, @Param("id") Long id);

    //根据主键Id查询是否为私密
    Boolean selectArticleIsPrivateById(@Param("id") Long id);

    //根据文章主键Id与用户Id查询指定的文章
    UserArticle selectAllByIdAndArticleAuthorId(@Param("id") Long id, @Param("articleAuthorId") Long articleAuthorId);

    //根据文章主键Id更新审核通过
    int updateArticleExamineByid(@Param("updatedArticleExamine")Boolean updatedArticleExamine,@Param("id")Long id);

    //查询当天已经完成的所有文章
    List<UserArticle> selectAllByArticleAuthorIdAndTime(@Param("from") Date from, @Param("to") Date to,@Param("id")Long id);

    //获取当前用户的各个文章数据集合
    List<UserArticleCountAll> selectUserAllById(@Param("userId") Long userId);

    //根据主键Id更新评论总数
    int updateArticleCommentById(@Param("id")Long id);


}
