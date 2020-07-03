package com.article.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.article.Entity.UserArticleComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserArticleCommentMapper {

    //根据文章Id获取整个评论的全部数据
    List<UserArticleComment> findAllByCommentArticleId(@Param("commentArticleId")Long commentArticleId);

    //插入一级评论
    int insert(UserArticleComment record);
}