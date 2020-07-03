package com.whc.cloud.Service;

import org.springframework.stereotype.Service;

@Service
public interface ResetService {

    //手机重置邮箱的验证码
    int resetEmail(String ssPhone,String username);

    //确认邮箱是否可以被插入
    int checkEmail(String ssPhone,String ssYzm,String ssEmail,String username);

    //更换新的手机号
    int updatePhone(String oldPhone,String phoneChangeYzm,String newPhone,String username);

    //验证个人信息是否存在
    int checkPerson(String resetName,String resetCode,String username);

    //控制1天的限时验证
    int timeCheck(String resetName,String username);

}
