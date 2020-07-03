package com.Controller;


import com.Service.UsernameToken;
import com.feign.UserMessageFeign;
import com.mapper.UserMapperToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserMessageController {

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;

    @Autowired
    UserMessageFeign userMessageFeign;

    //查询一共有多少个未读消息传给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectMsgOffFeign")
    public Map<String,Object> selectMsgOffFeign(){
        //查询一共有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（总体首页计算）
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userMessageFeign.selectMsgOff(id);
    }

    //查询一共有多少个未读消息传给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectEveryOneOffFeign")
    public Map<String,Object> selectEveryOneOffFeign(){
        //查询各个消息表有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)（消息首页计算）
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userMessageFeign.selectEveryOneOff(id);
    }

    //更新所有系统消息均为已读
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/updateSysMsgOnFeign")
    public void updateSysMsgOnFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        userMessageFeign.updateSysMsgOn(id);
    }

    //查询一共有多少系统消息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectAllSysMsgFeign")
    public Map<String,Object> selectAllSysMsgFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id=userMapperToken.getId(username);
        return userMessageFeign.selectAllSysMsg(id);
    }
}
