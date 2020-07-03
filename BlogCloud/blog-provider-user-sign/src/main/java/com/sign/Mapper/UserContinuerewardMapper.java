package com.sign.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.sign.Entity.UserContinuereward;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserContinuerewardMapper {

    //测试
    int insert(UserContinuereward record);

    //获得连续奖励领取状况
    UserContinuereward selectAllByContinueId(@Param("continueId")Long continueId);

    //更新第一个奖励的值
    void updateContinuerewardOneByContinueId(@Param("continueId")Long continueId);

    //更新第二个奖励的值
    void updateContinuerewardTwoByContinueId(@Param("continueId")Long continueId);

    //更新第三个奖励的值
    void updateContinuerewardThreeByContinueId(@Param("continueId")Long continueId);

    //更新第四个奖励的值
    void updateContinuerewardFourByContinueId(@Param("continueId")Long continueId);

    //更新第五个奖励的值
    void updateContinuerewardFiveByContinueId(@Param("continueId")Long continueId);

    //更新第六个奖励的值
    void updateContinuerewardSixByContinueId(@Param("continueId")Long continueId);

    //更新第七个奖励的值
    void updateContinuerewardSevenByContinueId(@Param("continueId")Long continueId);


}