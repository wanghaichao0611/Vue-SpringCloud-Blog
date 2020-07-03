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

//feign负载均衡用户的博客名(用户信息表)
@FeignClient(value = "blog-provider-user",fallbackFactory = AuthorBlogNameFallback.class)
public interface AuthorBlogNameFeign {

    //查询发表博客人的用户名
    @RequestMapping(value = "/getAuthorBlogNameAll", method = RequestMethod.POST)
    public Map<String,Object> getAuthorBlogNameAll(@RequestParam("authorIdList") String authorId);
}
@Component
class AuthorBlogNameFallback implements FallbackFactory<AuthorBlogNameFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorBlogNameFallback.class);

    @Override
    public AuthorBlogNameFeign create(Throwable throwable) {
        return new AuthorBlogNameFeign() {

            //查表发表博客人的用户名的fallback
            @Override
            public Map<String, Object> getAuthorBlogNameAll(String authorId) {
                AuthorBlogNameFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }
        };
    }
}
