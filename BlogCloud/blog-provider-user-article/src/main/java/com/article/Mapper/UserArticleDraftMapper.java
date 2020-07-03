package com.article.Mapper;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.article.Entity.UserArticleDraft;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserArticleDraftMapper {

    //插入草稿箱
    int insert(UserArticleDraft record);

    //根据用户Id查询草稿箱
    UserArticleDraft selectAllByUserId(@Param("userId")Long userId);

    //根据主键Id更新草稿箱
    int updateDraftById(@Param("updatedDraftTitle")String updatedDraftTitle,@Param("updatedDraftContent")String updatedDraftContent,@Param("updatedDraftTime")Date updatedDraftTime,@Param("id")Long id);

    //删除草稿箱中的文章
    int deleteByUserId(@Param("userId")Long userId);


}