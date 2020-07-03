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

@FeignClient(name = "blog-provider-user-member",fallbackFactory = PayMemberFallback.class )
public interface PayMemberFeign {

    //VIP年费的开通与续费
    @RequestMapping(value = "/vipYear", method = RequestMethod.POST)
    public Map<String,Object> vipYear(@RequestParam("id") Long id);

    //VIP包季的开通与续费
    @RequestMapping(value = "/vipThreeMonths", method = RequestMethod.POST)
    public Map<String,Object> vipThreeMonths(@RequestParam("id") Long id);

    //VIP包月的开通与续费
    @RequestMapping(value = "/vipMonth", method = RequestMethod.POST)
    public Map<String,Object> vipMonth(@RequestParam("id") Long id);

    //SVIP年费的开通与续费
    @RequestMapping(value = "/svipYear", method = RequestMethod.POST)
    public Map<String,Object> svipYear(@RequestParam("id") Long id);

    //SVIP包季的开通与续费
    @RequestMapping(value = "/svipThreeMonths", method = RequestMethod.POST)
    public Map<String,Object> svipThreeMonths(@RequestParam("id") Long id);

    //SVIP包月的开通与续费
    @RequestMapping(value = "/svipMonth", method = RequestMethod.POST)
    public Map<String,Object> svipMonth(@RequestParam("id") Long id);

    //余额充值指定月份的VIP
    @RequestMapping(value = "/balanceVip", method = RequestMethod.POST)
    public void balanceVip(@RequestParam("time")Integer time,@RequestParam("id") Long id);

    //余额充值指定月份的SVIP
    @RequestMapping(value = "/balanceSvip", method = RequestMethod.POST)
    public void balanceSvip(@RequestParam("time")Integer time,@RequestParam("id") Long id);
}
@Component
class PayMemberFallback implements FallbackFactory<PayMemberFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(PayMemberFallback.class);

    @Override
    public PayMemberFeign create(Throwable throwable) {
        return new PayMemberFeign(){

            //VIP年费的开通与续费的fallback
            @Override
            public Map<String, Object> vipYear(Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //VIP包季的开通与续费的fallback
            @Override
            public Map<String, Object> vipThreeMonths(Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //VIP包月的开通与续费的fallback
            @Override
            public Map<String, Object> vipMonth(Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //SVIP年费的开通与续费的fallback
            @Override
            public Map<String, Object> svipYear(Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //SVIP包季的开通与续费的fallback
            @Override
            public Map<String, Object> svipThreeMonths(Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //SVIP包月的开通与续费的fallback
            @Override
            public Map<String, Object> svipMonth(Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //余额充值指定月份的VIP的fallback
            @Override
            public void balanceVip(Integer time, Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

            //余额充值指定月份的SVIP的fallback
            @Override
            public void balanceSvip(Integer time, Long id) {
                PayMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
            }
        };
    }
}