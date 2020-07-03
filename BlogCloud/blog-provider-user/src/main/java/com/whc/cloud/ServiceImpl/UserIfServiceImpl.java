package com.whc.cloud.ServiceImpl;

import com.whc.cloud.Mapper.UserIfMapper;
import com.whc.cloud.Mapper.UserMapper;
import com.whc.cloud.Service.UserIfService;
import com.whc.cloud.User.User;
import com.whc.cloud.User.UserIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserIfServiceImpl implements UserIfService {

    //个人信息的表
    @Autowired
    UserIfMapper userIfMapper = null;

    //登录验证的表
    @Autowired
    UserMapper userMapper = null;

    //失败
    private static final int FAILED = 0;
    //成功
    private static final int SUCCESS = 1;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int information(UserIf userIf,Long id) {
        //先查询是否存在注册名
        List<User> userAll = userMapper.selectAllById(id);
        if (userAll.isEmpty()) {
            return FAILED;
        } else {
            //查询添加的博客名是否存在
            List<UserIf> realName = userIfMapper.selectAllByRealname(userIf.getBlogname());
            if (realName.isEmpty()) {
                //没有重复的话，就插入传过来的信息
                userIfMapper.updateByUsername(userIf,id);
                return SUCCESS;
            } else {
                return FAILED;
            }
        }
    }

    //从文章表负载均衡查询用户的博客名
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getBlogNameAll(String authorId) {
        String result = "";
        if (authorId.length() > 0) {
            String[] ifId = authorId.split(",");
            for (String id : ifId) {
                String blogName = userIfMapper.selectBlognameByIfId(Long.parseLong(id));
                result =result.toString()+blogName + ",";
            }
        }
        return result;
    }

    //更新紫色名字
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateName(Long id) {
        //查询安全信息
        String name=userIfMapper.selectBlognameByIfId(id);
        String realName="<span style=\"color: #FF00FF\">"+name+"<span>";
        //跟新紫色名字
        userIfMapper.updateBlognameByIfId(realName,id);
    }

    //失效紫色名字
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void deleteName(Long id) {
        //查询安全信息
        String name=userIfMapper.selectBlognameByIfId(id);
        //剔出<html>的标签
        String realName = name.replaceAll("</?[^>]+>", "");
        //失效紫色名字
        userIfMapper.updateBlognameByIfId(realName,id);
    }
}
