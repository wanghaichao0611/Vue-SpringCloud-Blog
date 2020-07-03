package com.article.feign;


import com.article.Entity.UserZeroSign;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



//增加文章的经验值
@FeignClient(name = "blog-provider-user-sign",fallbackFactory = ArticleSignFallback.class )
public interface ArticleSignFeign {

    //增加文章的经验值
    @RequestMapping(value = "/articleExperience", method = RequestMethod.POST)
    public void articleExperience(@RequestParam("id")Long id);

    //重置所有的奖励
    @RequestMapping(value = "/deleteArticleSign", method = RequestMethod.POST)
    public void deleteArticleSign(@RequestParam("userId") Long userId);

    //更新凌晨文章的奖励
    @RequestMapping(value = "/updateZero", method = RequestMethod.POST)
    public void updateZero(@RequestBody UserZeroSign userZeroSign,@RequestParam("id")Long id);

    //完成点赞每日任务
    @RequestMapping(value = "/thumpSign", method = RequestMethod.POST)
    public void thumpSign(@RequestParam("userId")Long userId);

}
@Component
class ArticleSignFallback implements FallbackFactory<ArticleSignFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(AuthorAvatarFallback.class);

    @Override
    public ArticleSignFeign create(Throwable throwable) {
        return new ArticleSignFeign() {

            //增加文章的经验值的fallback
            @Override
            public void articleExperience( Long id) {
                ArticleSignFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

            //删除所有的奖励的fallback
            @Override
            public void deleteArticleSign(Long userId) {
                ArticleSignFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

            //更新凌晨文章的奖励的fallback
            @Override
            public void updateZero(UserZeroSign userZeroSign, Long id) {
                ArticleSignFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

            //完成点赞每日任务的fallback
            @Override
            public void thumpSign(Long userId) {
                ArticleSignFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

        };
    }
}
