package com.whc.cloud.Service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

    //验证登录
    public int login(String username, String password);

    //账号注册
    public int register(String newUsername,String newPassword);

    //修改密码
    public int xiugaimima(String usernameXGMM,String oldPassword,String newPassword);
}
