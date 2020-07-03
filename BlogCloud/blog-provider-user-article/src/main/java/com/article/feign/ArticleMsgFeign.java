package com.article.feign;


import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "blog-provider-user-message", fallbackFactory = ArticleMsgFallback.class)
public interface ArticleMsgFeign {

    //发送别人的点赞消息
    @RequestMapping(value = "/thumpMsg", method = RequestMethod.POST)
    public void thumpMsg(@RequestParam("thumpId")Long thumpId,@RequestParam("thumpToId")Long thumpToId,
                         @RequestParam("articleId")Long articleId,@RequestParam("articleTitle")String articleTitle);

}
@Component
class ArticleMsgFallback implements FallbackFactory<ArticleMsgFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(ArticleMsgFallback.class);

    @Override
    public ArticleMsgFeign create(Throwable throwable) {
        return new ArticleMsgFeign() {

            //发送别人的点赞消息的fallback
            @Override
            public void thumpMsg(Long thumpId, Long thumpToId, Long articleId, String articleTitle) {
                ArticleMsgFallback.LOGGER.info("造成回退的原因是:",throwable);
            }
        };
    }
}
