package com.sign.Mapper;

import com.sign.Entity.UserSign;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.Date;import java.util.List;

@Mapper
public interface UserSignMapper {

    //测试
    int insert(UserSign record);

    //前端获取整个签到值
    UserSign selectAllBySignId(@Param("id")Long id);

    //判断是否是首次插入(经验值是否为null)
    Long selectContinueBySignId(@Param("id")Long id);

    //当经验为null的时直接首次签到(非会员)
    int updateFirstExperienceBySignId(@Param("id")Long id);

    //当经验为null的时直接首次签到(普通会员)
    int updateFirstExperienceVipBySignId(@Param("id")Long id);

    //当经验为null的时直接首次签到(超级会员)
    int updateFirstExperienceSvipBySignId(@Param("id")Long id);

    //获取本次时间和上次更新的天数差是否大于>=2
    Date selectRecentsigntimeBySignId(@Param("id")Long id);

    //如果存在大于等于2的情况,则说明需要重置签到连续次数并且+1的同时增加3经验和总签到+1
    int updateOverExperienceBySignId(@Param("id")Long id);

    //mybatis经验值的存储过程(非会员)
    void callExperience(@Param("id")Integer id);

    //mybatis经验值的存储过程(普通会员)
    void callVipExperience(@Param("id")Integer id);

    //mybatis经验值的存储过程(超级会员)
    void callSvipExperience(@Param("id")Integer id);

    //获取经验给前端
    Long selectExperienceBySignId(@Param("id")Long id);

    //查询所有的经验值(不为null)
    List<Long> selectExperience();

    //获取连续签到的主键
    Long selectIdBySignId(@Param("id")Long id);

    //连续签到增加经验值+20
    void updateContinueOneBySignId(@Param("id")Long id);

    //连续签到增加经验值+30
    void updateContinueTwoBySignId(@Param("id")Long id);

    //连续签到增加经验值+500
    void updateContinueFiveBySignId(@Param("id")Long id);

    //获得总数签到得次数
    Long selectTotalSignBySignId(@Param("id")Long id);

    //总数签到增加的经验值+100
    void updateTotalOneBySignId(@Param("id")Long id);

    //总数签到增加的经验值+200
    void updateTotalTwoBySignId(@Param("id")Long id);

    //总数签到增加的经验值+300
    void updateTotalThreeBySignId(@Param("id")Long id);

    //总数签到增加的经验值+500
    void updateTotalFiveBySignId(@Param("id")Long id);

    //总数签到增加的经验值+2000
    void updateTotalSixBySignId(@Param("id")Long id);

    //总数签到增加的经验值+5000
    void updateTotalSevenBySignId(@Param("id")Long id);

    //根据主键Id更新经验值
    int updateExperienceById(@Param("updatedExperience")Long updatedExperience,@Param("id")Long id);

    //根据用户Id更新经验值
    int updateExperienceBySignId(@Param("updatedExperience")Long updatedExperience,@Param("signId")Long signId);

}