package com.whc.cloud.Service;

import org.springframework.stereotype.Service;


@Service
public interface MailService {

    //根据Token传过来的username查询邮箱返回给前端
    public String getEmailByUsername(String username);

    //发送邮箱验证的验证码
    public int sendEmailNumber(String email);

    //根据前端传过来的邮箱和验证码来进行判断,Token获得的username来进行插入操作
    public int checkEmail(String email,String emailYzm,String username);

    //通过传过来的Token的双向绑定的username和邮箱,来发送重置密码的验证码
    public int updateEmail(String username,String chEmail);

    //通过传过来的Token双向绑定的username和邮箱，来重置密码。
    public int updatePassword(String chEmail,String passwordYzm,String chPassword,String username);



}
