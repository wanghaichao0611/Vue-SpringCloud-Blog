package com.member.Mapper;

import java.util.List;
import com.member.Entity.UserMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserMemberMapper {
    //测试
    int insert(UserMember record);

    //查询是否存在VIP会员
    Integer selectMemberVipByMemberId(@Param("memberId")Long memberId);

    //更新VIP的天数且更改标志
    int updateRechargetimeVipAndExpiretimeVipByMemberId(@Param("updatedRechargetimeVip") Date updatedRechargetimeVip, @Param("updatedExpiretimeVip") Date updatedExpiretimeVip, @Param("memberId") Long memberId);

    //续费VIP年费的天数
    int updateExpiretimeVipByMemberId(@Param("updatedExpiretimeVip") Date updatedExpiretimeVip, @Param("memberId") Long memberId);

    //查询VIP会员到期的时间
    Date selectExpiretimeVipByMemberId(@Param("memberId")Long memberId);

    //查询整个会员表
    UserMember selectAllByMemberId(@Param("memberId")Long memberId);

    //查询是否存在SVIP会员
    Integer selectMemberSvipByMemberId(@Param("memberId")Long memberId);

    //更新SVIP的天数且更改标志
    int updateRechargetimeSvipAndExpiretimeSvipByMemberId(@Param("updatedRechargetimeSvip")Date updatedRechargetimeSvip,@Param("updatedExpiretimeSvip")Date updatedExpiretimeSvip,@Param("memberId")Long memberId);

    //续费SVIP的天数
    int updateExpiretimeSvipByMemberId(@Param("updatedExpiretimeSvip")Date updatedExpiretimeSvip,@Param("memberId")Long memberId);

    //查询SVIP会员到期的时间
    Date selectExpiretimeSvipByMemberId(@Param("memberId")Long memberId);

    //查询数据库中普通会员的失效的Id
    List<Long> selectVipIdList();

    //查询数据库中超级会员的失效的Id
    List<Long> selectSVipIdList();

    //更新普通会员的标志
    int updateMemberVipByMemberId(@Param("memberId")Long memberId);

    //更新超级会员的标志
    int updateMemberSvipByMemberId(@Param("memberId")Long memberId);

    //更新紫色名字，我不想再负载均衡了
    int updateName(@Param("memberId")Long memberId);

}