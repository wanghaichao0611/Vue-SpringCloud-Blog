package com.feign;

import com.Entity.UserIf;

import com.requset.IAcsTokenRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//用于创建Ribbon负载均衡器
//feign默认已经整合了Hystrix，从Spring Cloud Dalston版本开始默认关闭，必须手动设置feign.hystrix.enable=true;
@FeignClient(name = "blog-provider-user",fallback = FeignClientFallback.class)
public interface UserFeignClient {

    //注册和修改个人信息的服务消费者(都必须要带Token)
    @RequestMapping(value = "/information", method = RequestMethod.POST)
    public Map<String, Object> personAdd(@RequestBody UserIf userIf,@RequestParam("id")Long id);


    //登录验证的服务消费者(原生不带Token写的暂时废弃)
    @RequestMapping(value = "/loginTest", method = RequestMethod.POST)
    public Map<String, Object> loginTest(@RequestParam("username") String username, @RequestParam("password") String password);

    //注册验证的服务消费者
    @RequestMapping(value = "/registerTest", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestParam("newUsername") String newUsername, @RequestParam("newPassword") String newPassword,
                                        @RequestBody IAcsTokenRequest iAcsTokenRequest);

    //修改密码的服务消费者(Token)
    @RequestMapping(value = "/xiugaimima", method = RequestMethod.POST)
    public Map<String, Object> xiugaimima(@RequestParam(value = "usernameXGMM", required = false) String usernameXGMM, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword);
}
@Component
class FeignClientFallback implements UserFeignClient
    {
        //注册用户的fallback
        @Override
        public Map<String, Object> register(String newUsername, String newPassword,IAcsTokenRequest iAcsTokenRequest) {
            Map<String, Object> feignMap = new HashMap<>();
            feignMap.put("message", "注册超时，请重新再试!");
            return feignMap;
        }

        //用户登录的fallback(暂时废弃使用)
        @Override
        public Map<String, Object> loginTest(String username, String password) {
            Map<String, Object> feignMap = new HashMap<>();
            feignMap.put("message", "登录超时，请重新再试!");
            return feignMap;
        }

        //用户信息注册的fallback(修改与注册)
        @Override
        public Map<String, Object> personAdd(UserIf userIf,Long id) {
            Map<String, Object> feignMap = new HashMap<>();
            feignMap.put("message", "注册超时，请重新再试!");
            return feignMap;
        }


        //修改密码的fallback
        @Override
        public Map<String, Object> xiugaimima(String usernameXGMM, String oldPassword, String newPassword) {
            Map<String, Object> feignMap = new HashMap<>();
            feignMap.put("message", "修改密码超时，请重新再试!");
            return feignMap;
        }
    }
