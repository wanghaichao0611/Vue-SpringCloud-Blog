package com.sign.Mapper;

import com.sign.Entity.UserSignArticle;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSignArticleMapper {
    int insert(UserSignArticle record);

    //根据用户Id查询所有信息
    UserSignArticle selectAllByUserId(@Param("userId") Long userId);

    //根据主键Id更新文章奖励
    int updateSignArticleById(@Param("updatedSignArticle") Integer updatedSignArticle, @Param("id") Long id);

    //查询主键的数量(暂不使用)
    int selectIdAll();

    //删除整个表根据用户Id
    int delete(@Param("userId") Long userId);

    //使点赞任务+1
    int updateThumpById(@Param("id") Long id);

}