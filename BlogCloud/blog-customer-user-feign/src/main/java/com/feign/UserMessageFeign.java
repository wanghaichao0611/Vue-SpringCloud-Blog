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

@FeignClient(name = "blog-provider-user-message",fallbackFactory = UserMessageFallback.class)
public interface UserMessageFeign {

    //查询一共有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（总体首页计算）
    @RequestMapping(value = "/selectMsgOff", method = RequestMethod.POST)
    public Map<String,Object> selectMsgOff(@RequestParam("id")Long id);

    //查询每个有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（消息首页计算）
    @RequestMapping(value = "/selectEveryOneOff", method = RequestMethod.POST)
    public Map<String,Object> selectEveryOneOff(@RequestParam("id")Long id);

    //更新所有系统消息均为已读
    @RequestMapping(value = "/updateSysMsgOn", method = RequestMethod.POST)
    public void updateSysMsgOn(@RequestParam("id")Long id);

    //查询一共有多少系统消息
    @RequestMapping(value = "/selectAllSysMsg", method = RequestMethod.POST)
    public Map<String,Object> selectAllSysMsg(@RequestParam("id")Long id);
}
@Component
class UserMessageFallback implements FallbackFactory<UserMessageFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserMessageFallback.class);

    @Override
    public UserMessageFeign create(Throwable throwable) {
        return new UserMessageFeign() {

            //查询一共有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（总体首页计算）的fallback
            @Override
            public Map<String, Object> selectMsgOff(Long id) {
                UserMessageFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取未读消息总数超时!");
                return feignMap;
            }

            //查询每个有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（消息首页计算）的fallback
            @Override
            public Map<String, Object> selectEveryOneOff(Long id) {
                UserMessageFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取未读消息总数超时!");
                return feignMap;
            }

            //更新所有系统消息均为已读的fallback
            @Override
            public void updateSysMsgOn(Long id) {
                UserMessageFallback.LOGGER.info("造成回退的原因是:",throwable);
            }

            //查询一共有多少系统消息的fallback
            @Override
            public Map<String,Object> selectAllSysMsg(Long id) {
                UserMessageFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取未读消息总数超时!");
                return feignMap;
            }
        };
    }
}
