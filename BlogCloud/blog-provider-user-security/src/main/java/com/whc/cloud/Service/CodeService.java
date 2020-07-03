package com.whc.cloud.Service;

import org.springframework.stereotype.Service;

@Service
public interface CodeService {

    //发送邮箱验证码
    public void sendEmailCode(String email);

    //发送手机验证码
    public void sendPhoneCode(String phone);

    //发送邮件的通知
    public void sendEmailMsg(Long id,String msg);
}
