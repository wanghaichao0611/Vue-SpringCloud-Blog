package com.pay.Mapper;

import com.pay.Entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface UserCouponMapper {

    //拆入优惠券信息
    int insert(UserCoupon record);

    //根据userId查询未使用过的优惠券
    List<UserCoupon> selectAllOffByUserId(@Param("userId") Long userId);

    //根据userId查询使用过的优惠券
    List<UserCoupon> selectAllOnByUserId(@Param("userId") Long userId);

    //根据id查询优惠券
    UserCoupon selectAllById(@Param("id") Long id);

    //根据主键Id更新使用标志
    int updateCouponOnById(@Param("updatedCouponOn")Boolean updatedCouponOn,@Param("id")Long id);

    //删除指定的优惠券
    int deleteById(@Param("id")Long id);

    //根据主键Id查询使用标志
    Boolean selectCouponOnById(@Param("id")Long id);



}