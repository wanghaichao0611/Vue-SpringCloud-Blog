package com.member.feign;


import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "blog-provider-user-security",fallbackFactory = MemberMsgFallback.class)
public interface MemberMsgFeign {

    //会员邮件通知
    @RequestMapping(value = "/emailMsg", method = RequestMethod.POST)
    public void emailMsg(@RequestParam("id")Long id, @RequestParam("msg")String msg);
}
@Component
class MemberMsgFallback implements FallbackFactory<MemberMsgFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(MemberMsgFallback.class);

    @Override
    public MemberMsgFeign create(Throwable throwable) {
        return new MemberMsgFeign() {

            @Override
            public void emailMsg(Long id, String msg) {
                MemberMsgFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

        };
    }
}