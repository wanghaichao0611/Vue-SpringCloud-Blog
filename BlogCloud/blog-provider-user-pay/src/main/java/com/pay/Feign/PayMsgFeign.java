package com.pay.Feign;


import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "blog-provider-user-message",fallbackFactory = PayMsgFallback.class)
public interface PayMsgFeign {

    //发送系统通知信息
    @RequestMapping(value = "/sendSysMsg", method = RequestMethod.POST)
    public void sendSysMsg(@RequestParam("title")String title,@RequestParam("msg")String msg, @RequestParam("id")Long id);
}
@Component
class PayMsgFallback implements FallbackFactory<PayMsgFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(PayMsgFallback.class);

    @Override
    public PayMsgFeign create(Throwable throwable) {
        return new PayMsgFeign() {

            //发送系统通知信息的fallback
            @Override
            public void sendSysMsg(String title,String msg, Long id) {
                PayMsgFallback.LOGGER.info("造成回退的原因是:",throwable);
            }
        };
    }
}
