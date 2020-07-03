package com.sign.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.sign.Entity.UserTotalreward;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTotalrewardMapper {

    //测试
    int insert(UserTotalreward record);

    //获取总数签到的领取情况
    UserTotalreward selectAllByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第一个奖励
    void updateTotalOneByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第二个奖励
    void updateTotalTwoByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第三个奖励
    void updateTotalThreeByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第三个奖励
    void updateTotalFourByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第三个奖励
    void updateTotalFiveByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第三个奖励
    void updateTotalSixByTotalId(@Param("totalId")Long totalId);

    //更新总数签到的第三个奖励
    void updateTotalSevenByTotalId(@Param("totalId")Long totalId);

}