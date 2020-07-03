package com.whc.cloud.Controller;

import com.whc.cloud.Feign.UserLoginFeign;
import com.whc.cloud.Service.UserService;
import com.whc.cloud.model.IAcsTokenRequest;
import com.whc.cloud.model.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    //Feign登录的负载均衡
    @Autowired
    private UserLoginFeign userLoginFeign;

    @Autowired
    UserService userService = null;

    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/loginTest")
    public Map<String, Object> loginTest(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {
        Map<String,Object> resultMap=new HashMap<>();
        int result=userService.login(username, password);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "登录成功":"账号或者密码错误");
        return resultMap;
    }

    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/registerTest")
    public ResponseTemplate register(@RequestParam("newUsername") String newUsername,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestBody IAcsTokenRequest iAcsTokenRequest) {
        int result = userService.register(newUsername, newPassword);
        if (result > 0) {
            //返还Token给前端
            return userLoginFeign.registerToken(newUsername, newPassword,iAcsTokenRequest);
        } else {
            return ResponseTemplate.builder()
                    .code(401)
                    .message("用户名已经存在")
                    .data(result)
                    .build();
        }
    }

    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/xiugaimima")
    public Map<String,Object> xiugaimima(@RequestParam(value = "usernameXGMM",required = false) String usernameXGMM,
                                         @RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword) {
        Map<String,Object> resultMap=new HashMap<>();
        int result=userService.xiugaimima(usernameXGMM,oldPassword,newPassword);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "修改成功":"密码不正确");
        return resultMap;
    }

}
