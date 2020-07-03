package com.Mapper;
import java.util.List;

import com.Entity.UserAvatar;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAvatarMapper {

    //测试
    int insert(UserAvatar record);

    //查询头像是否存在
    String selectAvatarNameByUsername(@Param("username")String username);

    //插入name-url到数据库
    int updateAvatarNameAndAvatarUrlByUsername(@Param("objectName")String objectName,@Param("url")String url,@Param("username")String username);

    //查询发表人博客的头像url
    String selectAvatarUrlByAvatarId(@Param("avatarId")Long avatarId);


}