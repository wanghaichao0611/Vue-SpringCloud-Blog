package com.Mapper;

import com.Entity.UserArticleImg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserArticleImgMapper {
    int insert(UserArticleImg record);
}