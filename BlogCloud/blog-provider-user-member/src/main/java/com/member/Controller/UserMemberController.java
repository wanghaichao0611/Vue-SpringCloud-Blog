package com.member.Controller;


import com.member.Entity.UserMember;
import com.member.Mapper.UserMemberMapper;
import com.member.Service.MemberService;
import com.member.Util.UtilDate;
import com.member.customer.RabbitMemberCustomer;
import com.member.feign.MemberMsgFeign;
import com.member.feign.MemberNameFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserMemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    UserMemberMapper userMemberMapper;

    @Autowired
    MemberMsgFeign memberMsgFeign;

    @Autowired
    MemberNameFeign memberNameFeign;

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserMemberController.class);

    //前端获取会员表
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/getMemberAll")
    public Map<String,Object> getMemberAll(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //根据Id查询会员表
        UserMember userMember=memberService.getAll(id);
        Integer memberVipSign=userMember.getMemberVip();
        Integer memberSvipSign=userMember.getMemberSvip();
        Date crudVipExpireTime=userMember.getExpiretimeVip();
        Date crudSvipExpireTime=userMember.getExpiretimeSvip();
        if (memberVipSign==1 && crudVipExpireTime!=null ){
            //计算剩余VIP时间返还给前端显示
           long vipExpireTime= UtilDate.getDayDifference(crudVipExpireTime);
           //如果时间小于<=0，则直接修改数据库中的会员标志，并且直接通知会员到期，二次保险，防止定时以及RabbitMq有问题。
            if (vipExpireTime==0 || vipExpireTime<0){
                //更新普通会员的标志
                userMemberMapper.updateMemberVipByMemberId(id);
                resultMap.put("vipSign", 0);
                resultMap.put("vipExpireTime","您未开通普通会员!");
                //通知用户普通会员到期(负载均衡)
                try {
                    String msg = "您的博客普通会员已经到期";
                    memberMsgFeign.emailMsg(id, msg);
                    //失效紫色名字
                    memberNameFeign.deleteName(id);
                }catch (Exception e){
                    LOGGER.info("邮箱又出问题了!");
                }
            }else {
                //防止二次更新导致的标志不一致
                resultMap.put("vipSign", userMember.getMemberVip());
                resultMap.put("vipExpireTime", vipExpireTime);
            }
        }else {
            //写的这么乱又多的原因是登录检验的二次保险，任选一种会员时间检测都可以。
            resultMap.put("vipSign", userMember.getMemberVip());
            resultMap.put("vipExpireTime","您未开通普通会员!");
        }
        if (memberSvipSign==1 && crudSvipExpireTime!=null ){
            //计算剩余SVIP时间返还给前端显示
            long svipExpireTime= UtilDate.getDayDifference(crudSvipExpireTime);
            //如果时间小于<=0，则直接修改数据库中的会员标志，并且直接通知会员到期，二次保险，防止定时以及RabbitMq有问题。
            if (svipExpireTime==0 || svipExpireTime<0){
                //更新超级会员的标志
                userMemberMapper.updateMemberSvipByMemberId(id);
                resultMap.put("svipSign", 0);
                resultMap.put("svipExpireTime", "您未开通超级会员!");
                //通知用户超级会员到期(负载均衡)
                try {
                    String msg = "您的博客超级会员已经到期";
                    memberMsgFeign.emailMsg(id, msg);
                    //失效紫色名字
                    memberNameFeign.deleteName(id);
                }catch (Exception e){
                    LOGGER.info("邮箱又出问题了!");
                }
            }else {
                //防止二次更新导致的标志不一致
                resultMap.put("svipSign", userMember.getMemberSvip());
                resultMap.put("svipExpireTime", svipExpireTime);
            }
        }else {
            //写的这么乱又多的原因是登录检验的二次保险，任选一种会员时间检测都可以。
            resultMap.put("svipSign", userMember.getMemberSvip());
            resultMap.put("svipExpireTime", "您未开通超级会员!");
        }
        //再负载均衡查询则色名字(不管以上的结果是如何都要查询名字)
        String realName=memberNameFeign.selectName(id);
        resultMap.put("blogName",realName);
        return resultMap;
    }

    //原始支付宝VIP年费支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/vipYear")
    public Map<String,Object> vipYear(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新天数并且返回成功标志给支付服务
        int result=memberService.vipYear(id);
        if (result==1){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }

    //原始支付宝VIP包季支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/vipThreeMonths")
    public Map<String,Object> vipThreeMonths(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新天数并且返回成功标志给支付服务
        int result=memberService.vipThreeMonths(id);
        if (result==1){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }


    //原始支付宝VIP包月支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/vipMonth")
    public Map<String,Object> vipMonth(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新天数并且返回成功标志给支付服务
        int result=memberService.vipMonth(id);
        if (result==1){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }

    //二维码支付的SVIP年费支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/svipYear")
    public Map<String,Object> svipYear(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新天数并且返回成功标志给支付服务
        int result=memberService.svipYear(id);
        if (result==1){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }

    //二维码支付SVIP包季支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/svipThreeMonths")
    public Map<String,Object> svipThreeMonths(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新天数并且返回成功标志给支付服务
        int result=memberService.svipThreeMonths(id);
        if (result==1){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }

    //二维码支付SVIP包月支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/svipMonth")
    public Map<String,Object> svipMonth(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新天数并且返回成功标志给支付服务
        int result=memberService.svipMonth(id);
        if (result==1){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }

    //负载均衡查询的会员标志
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectMember")
    public Map<String,Object> selectMember(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //查询会员标志
        UserMember userMember=userMemberMapper.selectAllByMemberId(id);
        //返回会员标志
        resultMap.put("vipMember",userMember.getMemberVip());
        resultMap.put("svipMember",userMember.getMemberSvip());
        return resultMap;
    }

    //负载均衡增加普通会员的时间
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/couponVipTime")
    public void couponVipTime(@RequestParam("id")Long id,@RequestParam("time")int time){
        //判断是开通还是添加奖励时间
        memberService.couponViptTime(id, time);
    }

    //负载均衡余额消费VIP
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/balanceVip")
    public void balanceVip(@RequestParam("time")Integer time,@RequestParam("id") Long id){
        //直接增加指定的时间并发送邮箱
        memberService.balanceVip(time, id);
    }

    //负载均衡余额消费VIP
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/balanceSvip")
    public void balanceSvip(@RequestParam("time")Integer time,@RequestParam("id") Long id){
        //直接增加指定的时间并发送邮箱
        memberService.balanceSvip(time, id);
    }

}
