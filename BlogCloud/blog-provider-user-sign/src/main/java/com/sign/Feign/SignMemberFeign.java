package com.sign.Feign;


import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "blog-provider-user-member",fallbackFactory = SignMemberFallback.class )
public interface SignMemberFeign {

    //根据Id查询会员开通情况
    @RequestMapping(value = "/selectMember", method = RequestMethod.POST)
    public Map<String,Object> selectMember(@RequestParam("id")Long id);

    //签到奖励分配的时间
    @RequestMapping(value = "/couponVipTime", method = RequestMethod.POST)
    public void couponVipTime(@RequestParam("id")Long id,@RequestParam("time")int time);

}
@Component
class SignMemberFallback implements FallbackFactory<SignMemberFeign> {

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(SignMemberFallback.class);

    @Override
    public SignMemberFeign create(Throwable throwable) {
        return new SignMemberFeign() {

            //根据Id查询会员开通情况的fallback
            @Override
            public Map<String, Object> selectMember(Long id) {
                SignMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询超时!");
                return feignMap;
            }

            //签到奖励分配的时间的fallback
            @Override
            public void couponVipTime(Long id, int time) {
                SignMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "领取超时!");
            }
        };
    }
}