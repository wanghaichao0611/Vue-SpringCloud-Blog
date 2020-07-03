package com.mapper;

import com.Entity.User;
import com.response.PhoneAndMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapperToken {

    //查询传过来的账号和密码是否相同
    @Select("select * from user where Username=#{username} and Password=#{password}")
    User getUser(@Param("username") String username, @Param("password") String password);

    //查询主键id
    Long getId(@Param("username") String username);

    //联表查询blogName返回给前端
    String selectBlogName(@Param("username") String username);

    //通过主键查询找到url返还给前端
    String selectUrl(@Param("id") Long id);

    //查询手机号和是否为任何一种会员(不想用负载均衡了)
    PhoneAndMember selectPhoneAndMember(@Param("id") Long id);

}