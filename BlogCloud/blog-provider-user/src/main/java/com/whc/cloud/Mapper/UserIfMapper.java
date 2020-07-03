package com.whc.cloud.Mapper;
import java.util.List;

import com.whc.cloud.User.User;
import org.apache.ibatis.annotations.Param;

import com.whc.cloud.User.UserIf;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserIfMapper {

    int insert(UserIf record);

    int updateByUsername(@Param("userIf")UserIf userIf,@Param("id")Long id);

    List<UserIf> selectAllByRealname(@Param("blogname")String blogname);

    String selectBlognameByIfId(@Param("ifId")Long ifId);

    int updateBlognameByIfId(@Param("updatedBlogname")String updatedBlogname,@Param("ifId")Long ifId);


}