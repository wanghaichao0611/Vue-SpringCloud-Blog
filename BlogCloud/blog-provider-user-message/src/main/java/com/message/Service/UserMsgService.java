package com.message.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public interface UserMsgService {

    //发送系统消息通知（若不接收消息提醒则设置不提醒）
    void sendSys(String title,String msg,Long id);

    //查询一共有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)
    int selectMsgOff(Long id);

    //查询各个消息表有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)
    int selectEveryOff(Long id);

    //更新所有系统消息均为已读
    void updateSysMsgOn(Long id);

    //发送点赞系统消息通知（若不接收消息提醒则设置不提醒）
    void sendThumpMsg(Long thumpId,Long thumpToId,Long articleId,String articleTitle);
}
