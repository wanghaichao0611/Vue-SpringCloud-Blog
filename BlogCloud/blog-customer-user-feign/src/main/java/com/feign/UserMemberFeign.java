package com.feign;

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

@FeignClient(name = "blog-provider-user-member",fallbackFactory = UserMemberFallback.class)
public interface UserMemberFeign {

    //前端获得整个会员表的信息
    @RequestMapping(value = "/getMemberAll", method = RequestMethod.POST)
    public Map<String,Object> getMemberAll(@RequestParam("id")Long id);

}
@Component
class UserMemberFallback implements FallbackFactory<UserMemberFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserMemberFallback.class);

    @Override
    public UserMemberFeign create(Throwable throwable) {
        return new UserMemberFeign() {
            @Override
            public Map<String, Object> getMemberAll(Long id) {
                UserMemberFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }
        };
    }
}