package com.member.customer;


import com.member.Entity.UserMember;
import com.member.Mapper.UserMemberMapper;
import com.member.Util.UtilDate;
import com.member.feign.MemberMsgFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//会员到期前用邮箱去提醒用户(普通会员与超级会员两个延迟队列等等再完善)
@Component
public class RabbitMemberCustomer {

    @Autowired
    MemberMsgFeign memberMsgFeign;

    @Autowired
    UserMemberMapper userMemberMapper;

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(RabbitMemberCustomer.class);

    //普通会员的监听
    @RabbitListener(queues = "vipTimeRealQueue")
    public void memberVipEmail(Long id){
        //查询普通会员是否已经到期
        UserMember userMember=userMemberMapper.selectAllByMemberId(id);
        long vipExpireTime= UtilDate.getDayDifference(userMember.getExpiretimeVip());
        if (vipExpireTime<=3){
            //通知用户即将到期
            try {
                String msg = "您的普通会员还有三天就要到期";
                memberMsgFeign.emailMsg(id, msg);
            }catch (Exception e){
                LOGGER.info("邮箱又出问题了!");
            }
        }
    }

    //超级会员的监听
   @RabbitListener(queues = "svipTimeRealQueue")
    public void memberSvipEmail(Long id){
        //查询超级会员是否已经到期
        UserMember userMember=userMemberMapper.selectAllByMemberId(id);
        long vipExpireTime= UtilDate.getDayDifference(userMember.getExpiretimeSvip());
        if (vipExpireTime<=3){
            //通知用户即将到期
            try {
                String msg = "您的超级会员还有三天就要到期";
                memberMsgFeign.emailMsg(id, msg);
            }catch (Exception e){
                LOGGER.info("邮箱又出问题了!");
            }
        }
    }
}
