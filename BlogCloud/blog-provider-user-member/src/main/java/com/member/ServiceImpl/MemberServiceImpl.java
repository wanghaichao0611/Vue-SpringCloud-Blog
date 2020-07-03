package com.member.ServiceImpl;

import com.member.Entity.UserMember;
import com.member.Mapper.UserMemberMapper;
import com.member.Service.MemberService;
import com.member.Util.MyMessagePostProcessor;
import com.member.Util.UtilDate;
import com.member.feign.MemberMsgFeign;
import com.member.feign.MemberNameFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;


//这个类可简化拆分，RabbitMQ发送类与时间增加类。
@Service
public class MemberServiceImpl implements MemberService {

    //失败
    private static final int FAILED = 0;
    //成功
    private static final int SUCCESS = 1;

    //会员的Crud
    @Autowired
    UserMemberMapper userMemberMapper;

    //RabbitMq的模板
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MemberMsgFeign memberMsgFeign;

    @Autowired
    MemberNameFeign memberNameFeign;

    //定义日志
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);

    //查询会员表的信息
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserMember getAll(Long id) {
        UserMember userMember = userMemberMapper.selectAllByMemberId(id);
        return userMember;
    }

    //VIP年费的开通
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int vipYear(Long id) {
        //首先查询是否存在会员
        Integer member = userMemberMapper.selectMemberVipByMemberId(id);
        //如果会员==0则说明为未开通,则直接开通VIP年费的天数
        if (member == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间加一年
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加一年
            cal.add(Calendar.YEAR, 1);
            //增加一年后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeVipAndExpiretimeVipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的年费普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        //如果会员==1则说明是续费服务，需要在ExpireTime+上一年的期限
        if (member == 1) {
            //查询到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeVipByMemberId(id);
            //获取时间加一年
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加一年
            cal.add(Calendar.YEAR, 1);
            //增加一年后的结果
            vipExpireTime = cal.getTime();
            //数据库更新到期时间增加一年,相当于续费
            userMemberMapper.updateExpiretimeVipByMemberId(vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的年费普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        return FAILED;
    }

    //VIP包季会员的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int vipThreeMonths(Long id) {
        //首先查询是否存在会员
        Integer member = userMemberMapper.selectMemberVipByMemberId(id);
        //如果会员==0则说明为未开通,则直接开通VIP包季的天数
        if (member == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包季
            cal.add(Calendar.MONTH, 3);
            //增加包季后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeVipAndExpiretimeVipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的三个月普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        //如果会员==1则说明是续费服务，需要在ExpireTime+包季的期限
        if (member == 1) {
            //查询VIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeVipByMemberId(id);
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包季
            cal.add(Calendar.MONTH, 3);
            //增加包季后的结果
            vipExpireTime = cal.getTime();
            //数据库更新到期时间增加三个月,相当于续费
            userMemberMapper.updateExpiretimeVipByMemberId(vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的三个月普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        return FAILED;
    }

    //VIP包月会员的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int vipMonth(Long id) {
        //首先查询是否存在会员
        Integer member = userMemberMapper.selectMemberVipByMemberId(id);
        //如果会员==0则说明为未开通,则直接开通VIP包月的天数
        if (member == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间加包月
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包月
            cal.add(Calendar.MONTH, 1);
            //增加包月后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeVipAndExpiretimeVipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的一个月普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();;
            return SUCCESS;
        }
        //如果会员==1则说明是续费服务，需要在ExpireTime+包月的期限
        if (member == 1) {
            //查询VIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeVipByMemberId(id);
            //获取时间加包月
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包月
            cal.add(Calendar.MONTH, 1);
            //增加包月后的结果
            vipExpireTime = cal.getTime();
            //数据库更新到期时间增加一月,相当于续费
            userMemberMapper.updateExpiretimeVipByMemberId(vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的一个月普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        return FAILED;
    }

    //SVIP年费会员的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int svipYear(Long id) {
        //首先查询是否存在超级会员
        Integer memberSvip = userMemberMapper.selectMemberSvipByMemberId(id);
        //如果超级会员==0则说明为未开通,则直接开通VIP年费的天数
        if (memberSvip == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间加一年
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加一年
            cal.add(Calendar.YEAR, 1);
            //增加一年后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeSvipAndExpiretimeSvipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的年费超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        //如果超级会员==1则说明是续费服务，需要在ExpireTime+上一年的期限
        if (memberSvip == 1) {
            //查询到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeSvipByMemberId(id);
            //获取时间加一年
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加一年
            cal.add(Calendar.YEAR, 1);
            //增加一年后的结果
            vipExpireTime = cal.getTime();
            //数据库更新到期时间增加一年,相当于续费
            userMemberMapper.updateExpiretimeSvipByMemberId(vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的年费超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        return FAILED;
    }

    //SVIP包季会员的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int svipThreeMonths(Long id) {
        //首先查询是否存在超级会员
        Integer memberSvip = userMemberMapper.selectMemberSvipByMemberId(id);
        //如果超级会员==0则说明为未开通,则直接开通VIP包季的天数
        if (memberSvip == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包季
            cal.add(Calendar.MONTH, 3);
            //增加包季后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeSvipAndExpiretimeSvipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的三个月超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        //如果超级会员==1则说明是续费服务，需要在ExpireTime+三个月的期限
        if (memberSvip == 1) {
            //查询SVIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeSvipByMemberId(id);
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包季
            cal.add(Calendar.MONTH, 3);
            //增加包季后的结果
            vipExpireTime = cal.getTime();
            userMemberMapper.updateExpiretimeSvipByMemberId(vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的三个月超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();;
            return SUCCESS;
        }
        return FAILED;
    }


    //SVIP包月会员的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int svipMonth(Long id) {
        //首先查询是否存在超级会员
        Integer memberSvip = userMemberMapper.selectMemberSvipByMemberId(id);
        //如果超级会员==0则说明为未开通,则直接开通VIP包月的天数
        if (memberSvip == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包月
            cal.add(Calendar.MONTH, 1);
            //增加包月后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeSvipAndExpiretimeSvipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的一个月超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        //如果超级会员==1则说明是续费服务，需要在ExpireTime+1个月的期限
        if (memberSvip == 1) {
            //查询SVIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeSvipByMemberId(id);
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加包月
            cal.add(Calendar.MONTH, 1);
            //增加包季月的结果
            vipExpireTime = cal.getTime();
            userMemberMapper.updateExpiretimeSvipByMemberId(vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的一个月超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
            return SUCCESS;
        }
        return FAILED;
    }

    //判断是开通还是添加奖励时间
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void couponViptTime(Long id, int time) {
        //首先查询是否存在会员
        Integer member = userMemberMapper.selectMemberVipByMemberId(id);
        if (member == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //开通奖励时间
            cal.add(Calendar.DATE, time);
            //增加奖励后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeVipAndExpiretimeVipByMemberId(vipRechargeTime, vipExpireTime, id);
            //更新会员的紫色名字(负载均衡)
            try {
                memberNameFeign.updateName(id);
            } catch (Exception e) {
                LOGGER.info("更新名字出现异常，放到日志补偿!");
            }
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
        }
        //如果会员==1则说明是续费服务，需要在ExpireTime+签到奖励的期限的期限
        if (member == 1) {
            //查询VIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeVipByMemberId(id);
            //获取到期时间
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加奖励的时间
            cal.add(Calendar.DATE, time);
            //增加奖励后的结果
            vipExpireTime = cal.getTime();
            //数据库更新到期时间增加奖励时间,相当于续费
            userMemberMapper.updateExpiretimeVipByMemberId(vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
        }
    }

    //余额充值VIP
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void balanceVip(Integer time, Long id) {
        //首先查询是否存在普通会员
        Integer member = userMemberMapper.selectMemberVipByMemberId(id);
        if (member == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取时间
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //开通奖励时间
            cal.add(Calendar.MONTH, time);
            //增加奖励后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeVipAndExpiretimeVipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的" + time + "个月普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
        }
        //如果会员==1则说明是续费服务，需要在ExpireTime+签到奖励的期限的期限
        if (member == 1) {
            //查询VIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeVipByMemberId(id);
            //获取到期时间
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加奖励的时间
            cal.add(Calendar.MONTH, time);
            //增加奖励后的结果
            vipExpireTime = cal.getTime();
            //数据库更新到期时间增加奖励时间,相当于续费
            userMemberMapper.updateExpiretimeVipByMemberId(vipExpireTime, id);
            //查询VIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余VIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeVip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("vipTimeRealExchange","vipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客的" + time + "个月普通会员，距离您的普通会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
        }
    }


    //余额充值SVIP
    @Override
    public void balanceSvip(Integer time, Long id) {
        //首先查询是否存在超级会员
        Integer memberSvip = userMemberMapper.selectMemberSvipByMemberId(id);
        //如果超级会员==0则说明为未开通,则直接开通SVIP的月数
        if (memberSvip == 0) {
            Date vipRechargeTime = new Date();
            Date vipExpireTime = new Date();
            //获取操作时间
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加指定的月份
            cal.add(Calendar.MONTH, time);
            //增加指定的月份后的结果
            vipExpireTime = cal.getTime();
            //数据库更新充值时间和到期时间
            userMemberMapper.updateRechargetimeSvipAndExpiretimeSvipByMemberId(vipRechargeTime, vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //更新会员的紫色名字(负载均衡)
                    try {
                        memberNameFeign.updateName(id);
                    } catch (Exception e) {
                        LOGGER.info("更新名字出现异常，放到日志补偿!");
                    }
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "开通了博客的"+time+"个月超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
        }
        //如果超级会员==1则说明是续费服务，需要在ExpireTime+time个月的期限
        if (memberSvip == 1) {
            //查询SVIP会员到期的时间
            Date vipExpireTime = userMemberMapper.selectExpiretimeSvipByMemberId(id);
            //获取时间加包季
            Calendar cal = Calendar.getInstance();
            //设置结束时间
            cal.setTime(vipExpireTime);
            //增加指定的月份
            cal.add(Calendar.MONTH, time);
            //增加指定的月份的结果
            vipExpireTime = cal.getTime();
            userMemberMapper.updateExpiretimeSvipByMemberId(vipExpireTime, id);
            //查询SVIP数据库到期时间
            UserMember userMember = userMemberMapper.selectAllByMemberId(id);
            //计算剩余SVIP时间(24*60*60*1000)
            long vipExpireRabbitMq = UtilDate.getDayDifference(userMember.getExpiretimeSvip());
            //转换时间秒(提前三天用邮箱提醒)
            Integer seconds = (int) (vipExpireRabbitMq - 3L) * (3600);
            //发送给RabbitMq到期前三天发邮件提醒用户(SVIP队列)
            MyMessagePostProcessor myMessagePostProcessor = new MyMessagePostProcessor(seconds*1000L);
            //发送到指定的队列并且设置指定的TTL时间给信息
            rabbitTemplate.convertAndSend("svipTimeRealExchange","svipTimeRealQueueKey",
                    id, myMessagePostProcessor);
            //控制台显示时间差与秒数
            LOGGER.info("天数：" + vipExpireRabbitMq + "，秒数：" + seconds);
            //另起线程完成更新，会员主业务已经完成
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //负载均衡发送开通邮件提醒
                    try {
                        String msg = "续费了博客"+time+"个月超级会员，距离您的超级会员的有效期还有" + vipExpireRabbitMq + "天";
                        memberMsgFeign.emailMsg(id, msg);
                    } catch (Exception e) {
                        LOGGER.info("邮箱又出问题了!");
                    }
                }
            }).start();
        }
    }
}
