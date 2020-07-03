package com.article.feign;

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

//feign负载均衡用户的头像(用户头像表)
@FeignClient(value = "blog-provider-user-upload",fallbackFactory = AuthorAvatarFallback.class)
public interface AuthorAvatarFeign {

    //查询发表博客人的头像URL
    @RequestMapping(value = "/getAuthorAvatarAll", method = RequestMethod.POST)
    public Map<String,Object> getAuthorAvatarAll(@RequestParam("authorIdList") String authorId);
}
@Component
class AuthorAvatarFallback implements FallbackFactory<AuthorAvatarFeign> {

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorAvatarFallback.class);

    @Override
    public AuthorAvatarFeign create(Throwable throwable) {
       return new AuthorAvatarFeign() {

           //查询发表博客人的头像URL的fallback
           @Override
           public Map<String, Object> getAuthorAvatarAll(String authorId) {
               AuthorAvatarFallback.LOGGER.info("造成回退的原因是:",throwable);
               Map<String, Object> feignMap = new HashMap<>();
               feignMap.put("message", "获取超时!");
               return feignMap;
           }
       };
    }
}