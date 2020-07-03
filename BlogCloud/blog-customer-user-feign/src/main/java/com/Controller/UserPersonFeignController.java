package com.Controller;

import com.Entity.UserIf;
import com.Service.UsernameToken;
import com.feign.UserFeignClient;

import com.mapper.UserMapperToken;
import com.requset.IAcsTokenRequest;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
public class UserPersonFeignController {

    //个人信息的feign负载均衡接口
    @Autowired
    private UserFeignClient userFeignClient;

    //Token双向绑定取username
    @Autowired
    private  HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;


    //登录验证的服务消费者
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam("username") String username,@RequestParam("password") String password){
        return this.userFeignClient.loginTest(username, password);
    }

    //注册验证的服务消费者
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/register")
    public Map<String,Object> register(@RequestParam("newUsername") String newUsername,@RequestParam("newPassword") String newPassword,
                                       IAcsTokenRequest iAcsTokenRequest){
        return this.userFeignClient.register(newUsername, newPassword,iAcsTokenRequest);
    }

    //注册的个人信息的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/personRegister")
    @AuthToken
    public Map<String,Object> informationRegister(UserIf userIf){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return this.userFeignClient.personAdd(userIf,id);
    }

    //修改的个人信息的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/personUpdate")
    @AuthToken
    public Map<String,Object> informationUpdate(UserIf userIf){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return this.userFeignClient.personAdd(userIf,id);
    }


    //修改密码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/updatePassword")
    @AuthToken
    public Map<String,Object> xiugaimima(@RequestParam(value = "usernameXGMM",required = false) String usernameXGMM, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword){
        String username=usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return this.userFeignClient.xiugaimima(username, oldPassword, newPassword);
    }
}
