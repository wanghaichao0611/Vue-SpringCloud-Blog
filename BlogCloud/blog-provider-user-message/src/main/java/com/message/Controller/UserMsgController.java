package com.message.Controller;


import com.message.Entity.UserMsgSystem;
import com.message.Entity.UserMsgThump;
import com.message.Mapper.UserMsgSystemMapper;
import com.message.Mapper.UserMsgThumpMapper;
import com.message.Service.UserMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserMsgController {

    @Autowired
    UserMsgService userMsgService;

    @Autowired
    UserMsgSystemMapper userMsgSystemMapper;

    @Autowired
    UserMsgThumpMapper userMsgThumpMapper;

    //查询一共有多少个未读消息传给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectMsgOff")
    public Map<String,Object> selectMsgOff(@RequestParam("id")Long id){
        Map<String,Object> resultMap=new HashMap<>();
        //查询一共有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)
        int result=userMsgService.selectMsgOff(id);
        resultMap.put("msgTotal",result);
        return resultMap;
    }

    //查询一共有多少个未读消息传给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectEveryOneOff")
    public Map<String,Object> selectEveryOneOff(@RequestParam("id")Long id){
        Map<String,Object> resultMap=new HashMap<>();
        //查询各个消息表有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（消息首页计算）
        //系统通知消息
        int msgTotal=userMsgService.selectEveryOff(id);
        //我收到的赞
        int thumpTotal=userMsgThumpMapper.selectThumpAll(id);
        resultMap.put("msgTotal",msgTotal);
        resultMap.put("thumpTotal",thumpTotal);
        return resultMap;
    }

    //更新所有系统消息均为已读
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/updateSysMsgOn")
    public void updateSysMsgOn(@RequestParam("id")Long id){
        userMsgService.updateSysMsgOn(id);
    }

    //负载均衡系统消息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/sendSysMsg")
    public void sendSysMsg(@RequestParam("title")String title,@RequestParam("msg")String msg, @RequestParam("id")Long id){
        //发送系统消息通知（若不接收消息提醒则不设置不提醒）
        userMsgService.sendSys(title,msg, id);
    }

    //查询一共有多少系统消息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectAllSysMsg")
    public Map<String,Object> selectAllSysMsg(@RequestParam("id")Long id){
        Map<String,Object> resultMap=new HashMap<>();
        //查询不管是否被阅读过的系统通知，时间为降序
        List<UserMsgSystem> userMsgSystemList=userMsgSystemMapper.selectAllByUserId(id);
        resultMap.put("success",true);
        resultMap.put("list",userMsgSystemList);
        return resultMap;
    }

    //发送别人的点赞消息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/thumpMsg")
    public void thumpMsg(@RequestParam("thumpId")Long thumpId,@RequestParam("thumpToId")Long thumpToId,
                         @RequestParam("articleId")Long articleId,@RequestParam("articleTitle")String articleTitle){
        //发送别人的点赞消息
        userMsgService.sendThumpMsg(thumpId, thumpToId, articleId, articleTitle);
    }
}
