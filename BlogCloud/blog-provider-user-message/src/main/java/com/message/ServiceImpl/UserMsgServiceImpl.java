package com.message.ServiceImpl;

import com.message.Config.WebSocket;
import com.message.Entity.UserMsgSecurity;
import com.message.Entity.UserMsgSystem;
import com.message.Entity.UserMsgThump;
import com.message.Mapper.UserMsgSecurityMapper;
import com.message.Mapper.UserMsgSystemMapper;
import com.message.Mapper.UserMsgThumpMapper;
import com.message.Service.UserMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class UserMsgServiceImpl implements UserMsgService {

    @Autowired
    UserMsgSecurityMapper userMsgSecurityMapper;

    @Autowired
    UserMsgSystemMapper userMsgSystemMapper;

    @Autowired
    WebSocket webSocket;

    @Autowired
    UserMsgThumpMapper userMsgThumpMapper;

    //控制台
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMsgServiceImpl.class);


    //发送系统消息通知（若不接收消息提醒则不设置不提醒）
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void sendSys(String title,String msg, Long id) {
        //根据用户ID查询整个消息安全表
        UserMsgSecurity userMsgSecurity=userMsgSecurityMapper.selectAllByUserId(id);
        //如果接收任何消息以及接收任何互动则需要用websocket提醒(也可以没有),不接受提醒直接设置未已读
        if (userMsgSecurity.getMsgOn().toString().equals("0") || userMsgSecurity.getBehaviorOn().toString().equals("0")){
            //提醒前端亮点有系统消息来了
            //webSocket.sendObjMessage(id.toString(),true);
            //更新消息到数据库中保存
            UserMsgSystem userMsgSystem=new UserMsgSystem();
            userMsgSystem.setUserId(id);
            userMsgSystem.setSystemTitle(title);
            userMsgSystem.setSystemMsg(msg);
            userMsgSystem.setReadOn(true);
            userMsgSystem.setCreatetime(new Date());
            //插入系统消息
            userMsgSystemMapper.insert(userMsgSystem);
            LOGGER.info("有一条系统通知来了,但是设置未已经读不提醒");
        }else {
            //更新消息到数据库中保存
            UserMsgSystem userMsgSystem = new UserMsgSystem();
            userMsgSystem.setUserId(id);
            userMsgSystem.setSystemTitle(title);
            userMsgSystem.setSystemMsg(msg);
            userMsgSystem.setReadOn(false);
            userMsgSystem.setCreatetime(new Date());
            //插入系统消息
            userMsgSystemMapper.insert(userMsgSystem);
        }
    }

    //查询一共有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int selectMsgOff(Long id) {
        //最后再完善，先查询有多少未读系统消息(最后完善可以五表连接查询)
        int sysOff=userMsgSystemMapper.selectMsgOff(id);
        int thumpOff=userMsgThumpMapper.selectThumpAll(id);
        return sysOff+thumpOff;
    }

    //查询各个消息表有多少个未读消息传给前端(评论，点赞，@，系统，好友通知)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int selectEveryOff(Long id) {
        //最后再完善，先查询有多少未读系统消息，各个表均需要分开才可以显示各个未读
        int sysOff=userMsgSystemMapper.selectMsgOff(id);
        return sysOff;
    }

    //更新所有系统消息均为已读
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateSysMsgOn(Long id) {
        userMsgSystemMapper.updateReadOnByUserId(id);
    }

    //发送点赞系统消息通知（若不接收消息提醒则设置不提醒）
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void sendThumpMsg(Long thumpId, Long thumpToId, Long articleId, String articleTitle) {
        //根据用户ID查询整个消息安全表(被点赞的Id)
        UserMsgSecurity userMsgSecurity=userMsgSecurityMapper.selectAllByUserId(thumpToId);
        //不接收消息提醒
        if (userMsgSecurity.getThumpmsgOn().toString().equals("0") || userMsgSecurity.getBehaviorOn().toString().equals("0")){
            UserMsgThump userMsgThump=new UserMsgThump();
            userMsgThump.setThumpId(thumpId);
            userMsgThump.setThumptoId(thumpToId);
            userMsgThump.setArticleId(articleId);
            userMsgThump.setArticleTitle(articleTitle);
            userMsgThump.setThumpmsgOn(true);
            userMsgThump.setCreatetime(new Date());
            userMsgThumpMapper.insert(userMsgThump);
        }else {
            UserMsgThump userMsgThump=new UserMsgThump();
            userMsgThump.setThumpId(thumpId);
            userMsgThump.setThumptoId(thumpToId);
            userMsgThump.setArticleId(articleId);
            userMsgThump.setArticleTitle(articleTitle);
            userMsgThump.setThumpmsgOn(false);
            userMsgThump.setCreatetime(new Date());
            userMsgThumpMapper.insert(userMsgThump);
        }
    }
}
