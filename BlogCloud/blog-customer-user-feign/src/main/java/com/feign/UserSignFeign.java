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

@FeignClient(name = "blog-provider-user-sign",fallbackFactory = UserSignFallback.class )
public interface UserSignFeign {


    //签到按钮的显示
    @RequestMapping(value = "/showSign", method = RequestMethod.POST)
    public Map<String, Object> showSign(@RequestParam("id") Long id);

    //获取所有排名和经验值
    @RequestMapping(value = "/getSignAll", method = RequestMethod.POST)
    public Map<String,Object> getSignAll(@RequestParam("id") Long id);

    //签到按钮的Button
    @RequestMapping(value = "/getExperience", method = RequestMethod.POST)
    public Map<String,Object> getExperience(@RequestParam("id") Long id);

    //连续签到的奖励
    @RequestMapping(value = "/getContinueReward", method = RequestMethod.POST)
    public Map<String, Object> getContinueReward(@RequestParam("id") Long id);

    //判断连续签到的按钮
    @RequestMapping(value = "/showContinue", method = RequestMethod.POST)
    public Map<String, Object> showContinue(@RequestParam("id") Long id);

    //判断总数签到的按钮
    @RequestMapping(value = "/showTotal", method = RequestMethod.POST)
    public Map<String, Object> showTotal(@RequestParam("id") Long id);

    //总数签到的奖励
    @RequestMapping(value = "/getTotalReward", method = RequestMethod.POST)
    public Map<String, Object> getTotalReward(@RequestParam("id") Long id);

    //查询当天文章完成的情况
    @RequestMapping(value = "/selectZero", method = RequestMethod.POST)
    public Map<String, Object> selectZero(@RequestParam("id") Long id);

}
@Component
class UserSignFallback implements FallbackFactory<UserSignFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserSignFallback.class);

    @Override
    public UserSignFeign create(Throwable throwable) {
        return new UserSignFeign() {

            //签到按钮的显示的fallback
            @Override
            public Map<String, Object> showSign(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //签到Button的fallback
            @Override
            public Map<String, Object> getExperience(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "签到超时!");
                return feignMap;
            }

            //获得排名和经验值fallback
            @Override
            public Map<String, Object> getSignAll(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //连续签到的奖励的fallback
            @Override
            public Map<String, Object> getContinueReward(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "领取超时!");
                return feignMap;
            }

            //连续签到按钮的显示的fallback
            @Override
            public Map<String, Object> showContinue(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "刷新超时!");
                return feignMap;
            }

            //总数签到按钮的显示的fallback
            @Override
            public Map<String, Object> showTotal(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "刷新超时!");
                return feignMap;
            }

            //总数签到的奖励的fallback
            @Override
            public Map<String, Object> getTotalReward(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "领取超时!");
                return feignMap;
            }

            //查询当天文章完成的情况
            @Override
            public Map<String, Object> selectZero(Long id) {
                UserSignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询超时!");
                return feignMap;
            }
        };
    }
}
