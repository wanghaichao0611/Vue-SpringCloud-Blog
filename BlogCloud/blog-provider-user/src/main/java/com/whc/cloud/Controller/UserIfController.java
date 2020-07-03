package com.whc.cloud.Controller;

import com.whc.cloud.Mapper.UserIfMapper;
import com.whc.cloud.Service.UserIfService;
import com.whc.cloud.User.UserIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserIfController {

    @Autowired
    UserIfService userIfService;

    @Autowired
    UserIfMapper userIfMapper;

    //注册的时候填写自己的信息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/information")
    public Map<String,Object> information(@RequestBody UserIf userIf,@RequestParam("id")Long id){
        Map<String,Object> objectMap=new HashMap<>();
        int result=userIfService.information(userIf,id);
        boolean flag=result>0;
        objectMap.put("success",flag);
        objectMap.put("message",flag? "注册成功":"未找到用户名或者博客名已被注册");
        return objectMap;
    }

    //文章负载均衡查询用户的博客名
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getAuthorBlogNameAll")
    public Map<String,Object> getAuthorBlogNameAll(@RequestParam("authorIdList") String authorId){
        Map<String,Object> objectMap=new HashMap<>();
        if (authorId==null){
            objectMap.put("success","failed");
            return objectMap;
        }
        String blogNameAll=userIfService.getBlogNameAll(authorId);
        objectMap.put("success", "success");
        objectMap.put("blogNameAll", blogNameAll);
        return objectMap;
    }

    //负载均衡更新紫色名字
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/updateName")
    public void updateName(@RequestParam("id")Long id){
        //更新紫色名字(应该为主键更新)
        userIfService.updateName(id);
    }

    //负载均衡查询紫色名字
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectName")
    public String selectName(@RequestParam("id")Long id){
        //查询紫色名字，也可以不是
        return userIfMapper.selectBlognameByIfId(id);
    }

    //负载均衡失效紫色名字
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/deleteName")
    public void deleteName(@RequestParam("id")Long id){
        //失效紫色名字(应该为主键更新)
        userIfService.deleteName(id);
    }
}
