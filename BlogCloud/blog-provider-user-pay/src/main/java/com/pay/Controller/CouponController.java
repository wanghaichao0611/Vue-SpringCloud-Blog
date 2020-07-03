package com.pay.Controller;


import com.github.pagehelper.PageInfo;
import com.pay.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CouponController {

    @Autowired
    CouponService couponService;


    //分页查询当前未使用的优惠券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectCouponOff")
    public Map<String, Object> selectCouponOff( @RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "count", defaultValue = "12") int count,
                                                @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //分页查询当前的优惠券
        PageInfo pageInfo=couponService.selectCouponOffAll(page, count, id);
        resultMap.put("success",true);
        resultMap.put("list",pageInfo.getList());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }


    //返还当前的金额券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/exchangeCoupon")
    public Map<String, Object> exchangeCoupon(@RequestParam("id")Long id,@RequestParam("orderId")Long orderId,
                                              @RequestParam("userId")Long userId){
        Map<String, Object> resultMap = new HashMap<>();
        //返还的券必须一致且未被使用过
        int result=couponService.exchangeCoupon(id, orderId, userId);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "兑换成功!":"非法兑换!");
        return resultMap;
    }


    //分页查询当前使用过的优惠券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectCouponOn")
    public Map<String, Object> selectCouponOn( @RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "count", defaultValue = "12") int count,
                                                @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //分页查询当前的优惠券
        PageInfo pageInfo=couponService.selectCouponOnAll(page, count, id);
        resultMap.put("success",true);
        resultMap.put("list",pageInfo.getList());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    //删除使用过的金额券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/deleteCoupon")
    public Map<String, Object> deleteCoupon(@RequestParam("id")Long id,@RequestParam("orderId")Long orderId,
                                              @RequestParam("userId")Long userId){
        Map<String, Object> resultMap = new HashMap<>();
        //返还的券必须一致且已经被使用了
        int result=couponService.deleteCoupon(id, orderId, userId);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "删除成功!":"非法删除!");
        return resultMap;
    }

}
