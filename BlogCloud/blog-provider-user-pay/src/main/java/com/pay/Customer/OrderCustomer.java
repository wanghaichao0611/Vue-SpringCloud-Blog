package com.pay.Customer;

import com.pay.Entity.UserCoupon;
import com.pay.Entity.UserOrder;
import com.pay.Feign.PayMsgFeign;
import com.pay.Feign.UserPaySecurityFeign;
import com.pay.Mapper.UserCouponMapper;
import com.pay.Mapper.UserOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class OrderCustomer {

    //定义消费者日志
    private static final Logger LOGGER= LoggerFactory.getLogger(OrderCustomer.class);

    @Autowired
    UserOrderMapper userOrderMapper;

    @Autowired
    UserPaySecurityFeign userPaySecurityFeign;

    @Autowired
    UserCouponMapper userCouponMapper;

    //发送信息的模板
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PayMsgFeign payMsgFeign;

    @RabbitListener(queues = "realQueue")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateOrder(Long id){
        //查询数据库中的Id支付情况
        UserOrder userOrder=userOrderMapper.selectAllById(id);
        //先检测实付金额
        BigDecimal money=userOrder.getReceiptAmount();
        if (money==null){
            //如果时间结束还未完成支付则支付失败
            userOrderMapper.updateReceiptAmountAndEndtimeById(new Date(),id);
            LOGGER.info("在规定时间内没有完成支付，本次交易失败");
        }else {
            //支付交易成功则需要查询是否是学生再返还给10%的金额券
            //负载均衡查询校园是否开通
            try {
                Map<String, Object> schoolResult = userPaySecurityFeign.selectSchool(userOrder.getOrderId());
                Object code = schoolResult.get("result");
                if (code.equals("success")) {
                    //如果是学生会员，返还的金额不需要失效期，不用放到延迟队列中。
                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setOrderId(id);
                    userCoupon.setUserId(userOrder.getOrderId());
                    userCoupon.setCouponName(userOrder.getBody() + "返还的10%的金额券");
                    BigDecimal bigDecimal = new BigDecimal(0.1);
                    userCoupon.setCouponMoney((userOrder.getReceiptAmount().multiply(bigDecimal)));
                    userCoupon.setCouponOn(false);
                    userCoupon.setCouponSubject("无时间限制");
                    userCoupon.setCreatetime(new Date());
                    //插入优惠券信息
                    userCouponMapper.insert(userCoupon);
                    //此时需要把开通成功以及含有优惠卷的信息用系统消息通知发给用户（负载均衡调用）（无限制）
                    payMsgFeign.sendSysMsg(userCoupon.getCouponName(),"您已经完成充值，并获得"+
                            userCoupon.getCouponMoney().intValue()+"元的10%返现券，祝您生活愈快！",userOrder.getOrderId());
                    LOGGER.info("我是学生，可以享受优惠");
                }else {
                    //可以享受随机立减（这个红包是有时间限制的30天）
                    Random random = new Random(10);
                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setOrderId(id);
                    userCoupon.setUserId(userOrder.getOrderId());
                    userCoupon.setCouponName(userOrder.getBody() + "随机立减的红包");
                    userCoupon.setCouponOn(false);
                    userCoupon.setCouponSubject("有效期30天");
                    userCoupon.setCreatetime(new Date());
                    if (money.intValue()<35){
                        //随机立减1-3
                        int a=random.nextInt(3)+1;
                        BigDecimal number = new BigDecimal(0);
                        number=BigDecimal.valueOf((int)a);
                        userCoupon.setCouponMoney(number);
                    }else if (money.intValue()<200){
                        //随机立减1-6
                        int a=random.nextInt(6)+1;
                        BigDecimal number = new BigDecimal(0);
                        number=BigDecimal.valueOf((int)a);
                        userCoupon.setCouponMoney(number);
                    }else {
                        //随机立减1-10
                        int a=random.nextInt(10)+1;
                        BigDecimal number = new BigDecimal(0);
                        number=BigDecimal.valueOf((int)a);
                        userCoupon.setCouponMoney(number);
                    }
                    //插入优惠券信息(返回优惠券的主键Id)
                    userCouponMapper.insert(userCoupon);
                    //再把优惠券放到RabbitMQ延迟队列30天中，过期后监听若是没有使用则直接报废.
                    rabbitTemplate.convertAndSend("realExchangeCoupon","realQueueKeyCoupon",userCoupon.getId());
                    //此时需要把开通成功以及含有优惠卷的信息用系统消息通知发给用户（负载均衡调用）(有时间限制)
                    payMsgFeign.sendSysMsg(userCoupon.getCouponName(),"您已经完成充值，并获得"+
                            userCoupon.getCouponMoney().intValue()+"元的立减返现红包，有效期30天注意使用，祝您生活愈快！",userOrder.getOrderId());
                    LOGGER.info("不是学生，无法享受优惠，但是可以享受随机立减");
                }
                LOGGER.info("本次交易支付成功");
            }catch (Exception e){
                LOGGER.info("返还优惠券失败，需要放入失败日志，补偿用户的损失！");
            }
        }
    }
}
