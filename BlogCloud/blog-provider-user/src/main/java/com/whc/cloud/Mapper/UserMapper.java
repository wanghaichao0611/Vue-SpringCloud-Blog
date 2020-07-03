package com.whc.cloud.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.whc.cloud.User.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    //创建新的联表信息,插入整个表的信息获取返回的ID键
    int insert(User record);

    //个人信息的插入
    int insertSelective(@Param("newUsername") String newUsername,@Param("newPassword") String newPassword);

    //注册的时候查询账号和密码是否存在
    List<User> selectBySnameAndSpassword(@Param("username")String username,@Param("password")String password);

    //查询账号是否存在
    List<User> selectSname(@Param("newUsername") String newUsername,@Param("newPassword") String newPassword);

    void updateSpassword(@Param("usernameXGMM") String usernameXGMM, @Param("oldPassword") String oldPassword,@Param("newPassword") String newPassword);

    ///查询是否完成注册
    List<User> selectAllById(@Param("id")Long id);

}