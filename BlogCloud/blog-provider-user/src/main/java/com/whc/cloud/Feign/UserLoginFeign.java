package com.whc.cloud.Feign;


import com.whc.cloud.model.IAcsTokenRequest;
import com.whc.cloud.model.ResponseTemplate;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


//用于创建Ribbon负载均衡器
//feign默认已经整合了Hystrix，从Spring Cloud Dalston版本开始默认关闭，必须手动设置feign.hystrix.enable=true;
@FeignClient(name = "blog-customer-user",fallbackFactory = UserLoginFallback.class)
public interface UserLoginFeign {

    @RequestMapping(value = "/registerToken", method = RequestMethod.POST)
    public ResponseTemplate registerToken(@RequestParam("newUsername")String username,@RequestParam("newPassword") String password,
                                          @RequestBody IAcsTokenRequest iAcsTokenRequest);
}
@Component
class UserLoginFallback implements FallbackFactory<UserLoginFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserLoginFallback.class);

    @Override
    public UserLoginFeign create(Throwable throwable) {
        return new UserLoginFeign() {
            @Override
            public ResponseTemplate registerToken(String username, String password,IAcsTokenRequest iAcsTokenRequest) {
               UserLoginFallback.LOGGER.info("造成回退的原因是:",throwable);;
                return ResponseTemplate.builder()
                        .code(401)
                        .message("注册失败，服务开小差了")
                        .build();
            }
        };
    }
}
