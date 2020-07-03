package com.message.Mapper;
import java.util.List;

import com.message.Entity.UserMsgSystem;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface UserMsgSystemMapper {

    //插入系统信息
    int insert(UserMsgSystem record);

    //查询有多少未读系统消息（数量）
    int selectMsgOff(@RequestParam("id") Long id);

    //更新所有系统消息均为已读
    int updateReadOnByUserId(@Param("userId") Long userId);

    //查询所有消息，不管是否已经被阅读，时间为降序
    List<UserMsgSystem> selectAllByUserId(@Param("userId")Long userId);

}