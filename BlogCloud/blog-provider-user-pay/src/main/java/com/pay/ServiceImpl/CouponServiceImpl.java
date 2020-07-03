package com.pay.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pay.Entity.UserCoupon;
import com.pay.Mapper.UserCouponMapper;
import com.pay.Mapper.UserWalletMapper;
import com.pay.Service.CouponService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CouponServiceImpl implements CouponService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //控制台
    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    UserCouponMapper userCouponMapper;

    @Autowired
    UserWalletMapper userWalletMapper;

    //分页查询当前的优惠券(未使用)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo selectCouponOffAll(int page,int count,Long id) {
        PageHelper.startPage(page,count);
        List<UserCoupon> couponList=userCouponMapper.selectAllOffByUserId(id);
        PageInfo pageInfo=new PageInfo(couponList);
        return pageInfo;
    }

    //返还的券必须一致且未被使用过(要上公平等待锁)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int exchangeCoupon(Long id,Long orderId, Long userId) {
        //公平锁实现排队兑换
        Lock lock=new ReentrantLock(true);
        //首先根据优惠券id查询指定的优惠券
        UserCoupon userCoupon=userCouponMapper.selectAllById(id);
        //根据用户Id再查询钱包Id
        Long walletId=userWalletMapper.selectIdByWalletId(userId);
        if (userCoupon==null){
            return FAILED;
        }else if (userCoupon.getCouponOn() || !userCoupon.getOrderId().equals(orderId)
                || !userCoupon.getUserId().equals(userId) ){
            return FAILED;
        }else {
            try{
                //公平上锁等待兑换
                lock.lock();
                //直接给余额增加
                userWalletMapper.updateWalletMoneyById(userCoupon.getCouponMoney(),walletId);
                //修改使用标志
                userCouponMapper.updateCouponOnById(true,id);
                return SUCCESS;
            }catch (Exception e){
                LOGGER.info("错误:"+e);
            }finally {
                //释放锁等其他人兑换
                lock.lock();
            }
        }
        return FAILED;
    }

    //分页查询当前的优惠券(未使用)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo selectCouponOnAll(int page,int count,Long id) {
        PageHelper.startPage(page,count);
        List<UserCoupon> couponList=userCouponMapper.selectAllOnByUserId(id);
        PageInfo pageInfo=new PageInfo(couponList);
        return pageInfo;
    }

    //删除已经使用过的兑换券
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int deleteCoupon(Long id,Long orderId, Long userId) {
        //首先根据优惠券id查询指定的优惠券
        UserCoupon userCoupon=userCouponMapper.selectAllById(id);
        if (userCoupon.getCouponOn() && userCoupon.getUserId().equals(userId)
                && userCoupon.getOrderId().equals(orderId)){
            //删除指定的优惠券
            userCouponMapper.deleteById(id);
            return SUCCESS;
        }
        return FAILED;
    }
}
