package com.member.job;

import com.member.Mapper.UserMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
@Lazy(false)
public class MemberJob{

    //用户会员的Mapper
    @Autowired
    UserMemberMapper userMemberMapper;

    //凌晨的定时任务(更新数据库中的失效的会员)
    @Scheduled(cron = "0 0 0 * * ？*")
    public void memberTime(){
       //查询数据库中普通会员中失效的会员Id
        List<Long> vipIdList=userMemberMapper.selectVipIdList();
        //查询数据库中超级会员失效的会员Id
        List<Long> svipIdList=userMemberMapper.selectSVipIdList();
        //直接更新数据库的会员标志
        for (int i=0;i<vipIdList.size();i++){
            Long vipId=vipIdList.get(i);
            //更新普通会员的标志
            userMemberMapper.updateMemberVipByMemberId(vipId);
            //通知用户的普通会员到期(负载均衡)

        }
        //直接更新数据库中的超级会员标志
        for (int i=0;i<svipIdList.size();i++){
            Long svipId=svipIdList.get(i);
            //更新超级会员的标志
            userMemberMapper.updateMemberSvipByMemberId(svipId);
            //通知用户的超级会员到期(负载均衡)
        }
    }
}