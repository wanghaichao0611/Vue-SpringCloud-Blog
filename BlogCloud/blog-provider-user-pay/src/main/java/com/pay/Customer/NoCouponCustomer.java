package com.pay.Customer;


import com.pay.Entity.UserOrder;
import com.pay.Feign.PayMsgFeign;
import com.pay.Mapper.UserCouponMapper;
import com.pay.Mapper.UserOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;


//什么都不给予奖励（支付宝充值余额）
@Configuration
public class NoCouponCustomer {

    //定义消费者日志
    private static final Logger LOGGER= LoggerFactory.getLogger(NoCouponCustomer.class);

    @Autowired
    UserOrderMapper userOrderMapper;

    @Autowired
    PayMsgFeign payMsgFeign;

    @RabbitListener(queues = "realQueueNoCoupon")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateNoCouponOrder(Long id){
        //查询数据库中的Id支付情况
        UserOrder userOrder=userOrderMapper.selectAllById(id);
        //先检测实付金额
        BigDecimal money=userOrder.getReceiptAmount();
        if (money==null){
            //如果时间结束还未完成支付则支付失败
            userOrderMapper.updateReceiptAmountAndEndtimeById(new Date(),id);
            LOGGER.info("在规定时间内没有完成支付，本次交易失败");
        }else {
            //交易成功，系统通知充值成功
            payMsgFeign.sendSysMsg("用户余额充值","您充值"+money+"已到账，注意查收!",userOrder.getOrderId());
            LOGGER.info("本次交易支付成功");
        }
    }
}
