package com.whc.cloud.Service;

import com.whc.cloud.User.UserIf;
import org.springframework.stereotype.Service;

@Service
public interface UserIfService {

    //注册插入自己的信息
    public int information(UserIf userIf,Long id);

    //从文章表负载均衡查询用户的博客名
    public String getBlogNameAll(String authorId);

    //更新紫色名字
    public void updateName(Long id);

    //失效紫色名字
    public void deleteName(Long id);
}
