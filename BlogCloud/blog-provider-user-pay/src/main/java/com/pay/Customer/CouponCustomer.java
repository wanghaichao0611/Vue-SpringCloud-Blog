package com.pay.Customer;


import com.pay.Entity.UserCoupon;
import com.pay.Feign.PayMsgFeign;
import com.pay.Mapper.UserCouponMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


//监听有效期的优惠券的使用情况（到期通知就行，若要提前三天再通知则需要把当前信息TTL设置两个）
@Service
public class CouponCustomer {

    //定义消费者日志
    private static final Logger LOGGER= LoggerFactory.getLogger(CouponCustomer.class);

    @Autowired
    UserCouponMapper userCouponMapper;

    @Autowired
    PayMsgFeign payMsgFeign;

    @RabbitListener(queues = "realQueueCoupon")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void selectCoupon(Long id){
        //根据优惠券主键Id查询使用所有信息
        UserCoupon userCoupon=userCouponMapper.selectAllById(id);
        if (!userCoupon.getCouponOn()){
            //如果没使用直接失效
            userCouponMapper.updateCouponOnById(true,id);
            //系统通知有期限的优惠券失效了
            payMsgFeign.sendSysMsg(userCoupon.getCouponName(),"您有一张"+userCoupon.getCouponMoney()+userCoupon.getCouponName()+"到期了!",id);
            LOGGER.info("优惠券:"+id+":失效了");
        }
    }
}
