package com.pay.Feign;

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

@FeignClient(name = "blog-provider-user-security",fallbackFactory = PaySecurityFallback.class)
public interface UserPaySecurityFeign {

    //手机号重置支付密码的确认验证码的验证服务
    @RequestMapping(value = "/checkServerCode", method = RequestMethod.POST)
    public Map<String,Object> checkServerCode(@RequestParam("payCode") String payCode,@RequestParam("username") String username);

    //根据用户Id查询校园注册情况
    @RequestMapping(value = "/selectSchool", method = RequestMethod.POST)
    public Map<String, Object> selectSchool(@RequestParam("id") Long id);
}
@Component
class PaySecurityFallback implements FallbackFactory<UserPaySecurityFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(PaySecurityFallback.class);

    @Override
    public UserPaySecurityFeign create(Throwable throwable) {
        return new UserPaySecurityFeign() {

            //手机号重置支付密码的确认验证码的验证服务fallback
            @Override
            public Map<String, Object> checkServerCode(String payCode, String username) {
                PaySecurityFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }


            //根据用户Id查询校园注册情况fallback
            @Override
            public Map<String, Object> selectSchool(Long id) {
                PaySecurityFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再查询!");
                return feignMap;
            }

        };
    }
}
