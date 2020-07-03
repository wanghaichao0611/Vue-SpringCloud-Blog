package com.article.feign;

import com.article.requset.EsRequest;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "blog-provider-user-es",fallbackFactory = EsFeignFallback.class)
public interface EsFeign {

    //把文章增加到指定的索引中
    @RequestMapping(value = "/addArticle", method = RequestMethod.POST)
    public Map<String, Object> addArticle(@RequestBody EsRequest esRequest);

    //删除Es中存在的文章
    @RequestMapping(value = "/deleteArticle", method = RequestMethod.POST)
    public Map<String, Object> deleteArticle(@RequestBody EsRequest esRequest);

    //更新文章的时候把更新的信息保持与Es中一致
    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    public Map<String, Object> updateArticle(@RequestBody EsRequest esRequest,@RequestParam("oldCategoryName")String oldCategoryName);
}
@Component
class EsFeignFallback implements FallbackFactory<EsFeign>{
    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(EsFeignFallback.class);
    @Override
    public EsFeign create(Throwable throwable) {
        return new EsFeign(){

            //添加到指定索引的fallback
            @Override
            public Map<String, Object> addArticle(EsRequest esRequest) {
                EsFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "添加超时!");
                return feignMap;
            }

            //删除Es中存在的文章的fallback
            @Override
            public Map<String, Object> deleteArticle(EsRequest esRequest) {
                EsFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "删除超时!");
                return feignMap;
            }

            //更新文章的时候把更新的信息保持与Es中一致的fallback
            @Override
            public Map<String, Object> updateArticle(EsRequest esRequest,String oldCategoryName) {
                EsFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时!");
                return feignMap;
            }
        };
    }
}
