package com.whc.cloud.Service;

import com.whc.cloud.User.NameCard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PhoneService {

    //当其他微服务需要验证码的时候需要用到这个服务提供者
    int sendPhoneCodeForEachServer(String username);

    //支付微服务手机验证码修改支付密码确认验证码返回给其他微服务的标志
    int checkServerPhone(String payCode,String username);

    //根据Token的username获得phone返还给前端
    String getPhoneByUsername(String username);

    //注册手机的验证码
    int registerPhoneCode(String phone);

    //确认注册手机(Token绑定手机号)
    int registerPhone(String phone,String phoneYzm,String username);

    //实名认证的验证码(Token绑定的username与phone)
    int registerCardCode(String phoneSFRZ,String username);

    //确认自己的实名认证信息
    int registerCard(String phoneSFRZ,String phoneCode,String realnameSFRZ,String cardNumber,String username);

    //获取认证成功的真实姓名返回给前端
    String getRealNameByUsername(String username);

    //获取认证成功的身份证返还给前端
    String getNameCardByUsername(String username);

    //手机重置密码的验证码
    int phoneReset(String phoneReset,String username);

    //确认手机的重置密码
    int phoneResetPassword(String phoneReset,String phoneResetCode,String phonePassword,String username);

    //确认实名认证的重置密码
    int cardResetPassword(String cardName,String cardCode,String cardPassword,String username);


}
