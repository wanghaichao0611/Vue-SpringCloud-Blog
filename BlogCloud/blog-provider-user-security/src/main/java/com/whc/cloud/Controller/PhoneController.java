package com.whc.cloud.Controller;

import com.whc.cloud.Service.MailService;
import com.whc.cloud.Service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

//和手机有关的都在这里面
@RestController
public class PhoneController {

    //查找邮箱的服务
    @Autowired
    MailService mailService;

    //手机的服务
    @Autowired
    PhoneService phoneService;

    //当其他微服务需要验证码的时候需要用到这个服务提供者
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/eachServerCode")
    public Map<String, Object> eachServerCode(@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //直接根据username获得手机号，并且直接发送验证码,保存在redis中.
        int result=phoneService.sendPhoneCodeForEachServer(username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "手机验证码已发送" : "服务器开了小差,稍微再试!");
        return resultMap;
    }

    //支付微服务手机验证码修改支付密码确认验证码返回给其他微服务的标志
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/checkServerCode")
    public Map<String,Object> checkServerCode(@RequestParam("payCode") String payCode,@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //确认username+获得的手机号是否存在与验证码的双向绑定存在的话返回成功的标志
        int result=phoneService.checkServerPhone(payCode, username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("result", flag ? "success" : "failed");
        return resultMap;
    }

    //隐藏手机给前端返还给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getPhone")
    public Map<String, Object> getPhone(@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        String myPhone = phoneService.getPhoneByUsername(username);
        if (myPhone!=null) {
            //隐藏手机给前端
            myPhone = myPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            resultMap.put("success", true);
            resultMap.put("message", myPhone);
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "您的手机还未完成绑定,请完成绑定!");
            return resultMap;
        }
    }

    //绑定手机的按钮验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneCode")
    public Map<String, Object> phoneCode(@RequestParam("phone") String phone) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = phoneService.registerPhoneCode(phone);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "手机验证码已发送" : "手机已被注册");
        return resultMap;
    }

    //绑定手机的验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneRegister")
    public Map<String, Object> phoneRegister(@RequestParam("phone") String phone, @RequestParam("phoneYzm") String phoneYzm, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        //如果验证码和phone正确验证就返还给前端加密手机
        if (phoneService.registerPhone(phone, phoneYzm, username) > 0) {
            //根据username+返还给前端保存的加密手机
            String myPhone = phoneService.getPhoneByUsername(username);
            //隐藏邮箱给前端
            myPhone = myPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            resultMap.put("success", true);
            resultMap.put("message", myPhone);
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "验证码错误,失效或手机有改动");
            return resultMap;
        }
    }

    //重置密码的手机验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneResetCardCode")
    public Map<String, Object> phoneResetCardCode(@RequestParam("phoneReset") String phoneReset, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = phoneService.phoneReset(phoneReset, username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "验证码已发送" : "手机未找到");
        return resultMap;
    }

    //判断手机的重置密码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneResetUpdate")
    public Map<String, Object> phoneResetUpdate(@RequestParam("phoneReset") String phoneReset, @RequestParam("phoneResetCode") String phoneResetCode,
            @RequestParam("phonePassword") String phonePassword,@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        //确认修改密码
        int result=phoneService.phoneResetPassword(phoneReset, phoneResetCode, phonePassword, username);
        //如果result>0则成修改密码
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "重置密码成功" : "验证码错误，失效或者手机有改动");
        return resultMap;
    }

    //实名重置密码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/cardReset")
    public Map<String, Object> cardReset(@RequestParam("cardName")String cardName,@RequestParam("cardCode")String cardCode,
                                         @RequestParam("cardPassword")String cardPassword,@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //确认修改密码
        int result=phoneService.cardResetPassword(cardName, cardCode, cardPassword, username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "重置密码成功" : "个人信息不匹配");
        return resultMap;
    }

        //实名验证的手机验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneCardCode")
    public Map<String, Object> phoneCardCode(@RequestParam("phoneSFRZ") String phoneSFRZ, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = phoneService.registerCardCode(phoneSFRZ, username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "验证码已发送" : "手机未找到");
        return resultMap;
    }

    //确认实名认证
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneCardRegister")
    public Map<String, Object> phoneCardRegister(@RequestParam("phoneSFRZ") String phoneSFRZ, @RequestParam("phoneCode") String phoneCode,
                                                 @RequestParam("realnameSFRZ") String realnameSFRZ, @RequestParam("cardNumber") String cardNumber, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = phoneService.registerCard(phoneSFRZ, phoneCode, realnameSFRZ, cardNumber, username);
        //如果result>0则表示可以把插入的信息发送给前端
        if (result > 0) {
            String realName=phoneService.getRealNameByUsername(username);
            String realCard = phoneService.getNameCardByUsername(username);
            if (realName!=null) {
                //返回你的加密信息给前端
                realName = realName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
                realCard = realCard.replaceAll("(\\d{1})\\d{16}(\\d{1})", "$1****$2");
                resultMap.put("success", true);
                resultMap.put("realName", realName);
                resultMap.put("realCard", realCard);
                return resultMap;
            }else {
                resultMap.put("success", false);
                resultMap.put("message", "获取超时!");
                return resultMap;
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "验证码错误，失效或者有身份证已经注册");
            return resultMap;
        }
    }


    //前端获得实名认证的信息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getNameCard")
    public Map<String, Object> getNameCard(@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        String realName=phoneService.getRealNameByUsername(username);
        String realCard = phoneService.getNameCardByUsername(username);
        if (realName !=null && realCard!=null) {
            //返回你的加密信息给前端
            realName = realName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            realCard = realCard.replaceAll("(\\d{1})\\d{16}(\\d{1})", "$1****$2");
            resultMap.put("success", true);
            resultMap.put("realCard", realCard);
            resultMap.put("realName", realName);
            return resultMap;
        }
            resultMap.put("success", false);
            resultMap.put("message", "请完成实名注册");
            return resultMap;
    }

    //前端获取整个安全验证
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSecurityAll")
    public Map<String, Object> getSecurityAll(@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //邮箱,手机和实名认证的消息
        String email=mailService.getEmailByUsername(username);
        String phone=phoneService.getPhoneByUsername(username);
        String card=phoneService.getNameCardByUsername(username);
        //1.均不未空值
        if (email!=null && phone!=null && card!=null){
            resultMap.put("sort",1);
        }
        //2.手机号为空值
        else if (email!=null && phone==null && card!=null){
            resultMap.put("sort",2);
        }
        //3.身份证为空值
        else if (email!=null && phone!=null && card==null){
            resultMap.put("sort",3);
        }
        //4.均为空值
        else if (email==null && phone==null && card==null){
            resultMap.put("sort",4);
        }
        //5.邮箱为空
        else if (email==null && phone!=null && card!=null){
            resultMap.put("sort",5);
        }
        //6.邮箱和手机为空值
        else if (email==null && phone==null && card!=null){
            resultMap.put("sort",6);
        }
        //7.邮箱和身份证为空值
        else if (email==null && phone!=null && card==null) {
            resultMap.put("sort",7);
        }
        //8.身份证和手机为空值
        else {
            resultMap.put("sort",8);
        }
        return resultMap;
    }
}
