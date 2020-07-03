package com.pay.Service;


import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public interface CouponService {

    //分页查询当前的优惠券(未使用)
    PageInfo selectCouponOffAll(int page,int count,Long id);

    //返还的券必须一致且未被使用过
    int exchangeCoupon(Long id,Long orderId, Long userId);

    //分页查询当前的优惠券(使用过)
    PageInfo selectCouponOnAll(int page,int count,Long id);

    //删除已经使用过的兑换券
    int deleteCoupon(Long id,Long orderId, Long userId);
}
