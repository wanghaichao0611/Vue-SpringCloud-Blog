package com.message.Mapper;

import com.message.Entity.UserMsgThump;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMsgThumpMapper {

    //插入点赞消息
    int insert(UserMsgThump record);

    //计算有多少未读的点赞消息总数
    int selectThumpAll(@Param("userId")Long userId);

}