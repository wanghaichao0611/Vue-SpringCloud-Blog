package com.member.Service;

import com.member.Entity.UserMember;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    //查询会员表的信息
    UserMember getAll(Long id);

    //VIP年费会员的更新
    int vipYear(Long id);

    //VIP包季会员的更新
    int vipThreeMonths(Long id);

    //VIP包月会员的更新
    int vipMonth(Long id);

    //SVIP年费会员的更新
    int svipYear(Long id);

    //SVIP包季会员的更新
    int svipThreeMonths(Long id);

    //SVIP包月会员的更新
    int svipMonth(Long id);

    //判断是开通还是添加奖励时间
    void couponViptTime(Long id,int time);

    //余额充值VIP
    void balanceVip(Integer time,Long id);

    //余额充值SVIP
    void balanceSvip(Integer time,Long id);
}
