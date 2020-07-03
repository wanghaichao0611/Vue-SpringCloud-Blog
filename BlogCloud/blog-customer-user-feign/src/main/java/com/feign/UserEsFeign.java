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

@FeignClient(name = "blog-provider-user-es",fallbackFactory = UserEsFallback.class)
public interface UserEsFeign {

    //第一个主页的搜索(page代表从第几条开始，count代表查询多少条,类似mysql的limit分页参数)
    @RequestMapping(value = "/esPageAll", method = RequestMethod.POST)
    public Map<String,Object> esPageAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "count", defaultValue = "5") int count,
                                             @RequestParam("categoryName") String categoryName,
                                             @RequestParam("keyWord") String  keyWord);

    //博客分类主页的搜索
    @RequestMapping(value = "/esCategoryAll", method = RequestMethod.POST)
    public Map<String,Object> esCategoryAll(@RequestParam("categoryName") String categoryName);
}
@Component
class UserEsFallback implements FallbackFactory<UserEsFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserEsFallback.class);

    @Override
    public UserEsFeign create(Throwable throwable) {
        return new UserEsFeign() {

            //第一个主页的搜索的fallback
            @Override
            public Map<String, Object> esPageAll(int page, int count, String categoryName, String keyWord) {
                UserEsFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "分类关键字搜索超时!");
                return feignMap;
            }

            //博客分类主页的搜索的fallback
            @Override
            public Map<String, Object> esCategoryAll(String categoryName) {
                UserEsFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "分类主页搜索超时!");
                return feignMap;
            }
        };
    }
}
