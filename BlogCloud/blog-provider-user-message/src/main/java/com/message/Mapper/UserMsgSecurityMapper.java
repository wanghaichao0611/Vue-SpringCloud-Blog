package com.message.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.message.Entity.UserMsgSecurity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMsgSecurityMapper {

    //测试
    int insert(UserMsgSecurity record);

    //根据用户Id查询信息安全
    UserMsgSecurity selectAllByUserId(@Param("userId")Long userId);

}