package com.whc.cloud.Controller;

import com.whc.cloud.Service.MailService;
import com.whc.cloud.Service.PhoneService;
import com.whc.cloud.Service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ResetController {

    //Email的服务
    @Autowired
    MailService mailService;

    //Phone的服务
    @Autowired
    PhoneService phoneService;

    //重置邮箱的服务
    @Autowired
    ResetService resetService;

    //手机重置邮箱的验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetEmail")
    public Map<String, Object> resetEmail(@RequestParam("ssPhone") String ssPhone, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = resetService.resetEmail(ssPhone,username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "验证码已发送" : "手机未找到");
        return resultMap;
    }

    //手机确认重置邮箱(并且返回给修改过的邮箱给前端)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetEmailUpdate")
    public Map<String, Object> resetEmailUpdate(@RequestParam("ssPhone") String ssPhone, @RequestParam("ssYzm") String ssYzm,
                                                @RequestParam("ssEmail") String ssEmail, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
       int result=resetService.checkEmail(ssPhone,ssYzm,ssEmail,username);
       if (result>0){
           //如果结果成功则需要把加密后的邮箱返还给前端
           String myEmail = mailService.getEmailByUsername(username);
               //隐藏邮箱给前端
               myEmail=myEmail.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
               resultMap.put("success", true);
               resultMap.put("message", myEmail);
               return resultMap;
       }else {
           resultMap.put("success", false);
           resultMap.put("message", "验证码错误，失效或邮箱已经被注册!");
           return resultMap;
       }
    }

    //更换旧手机号的Button
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/changePhone")
    public Map<String, Object> changePhone(@RequestParam("oldPhone") String oldPhone, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = resetService.resetEmail(oldPhone,username);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "验证码已发送" : "手机未找到");
        return resultMap;
    }

    //更新新的手机号
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/changePhoneUpdate")
    public Map<String, Object> changePhoneUpdate(@RequestParam("oldPhone") String oldPhone, @RequestParam("phoneChangeYzm") String phoneChangeYzm,
                                                 @RequestParam("newPhone") String newPhone,@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        int result=resetService.updatePhone(oldPhone, phoneChangeYzm, newPhone, username);
        if (result>0){
            //根据username+返还给前端保存的加密手机
            String myPhone = phoneService.getPhoneByUsername(username);
            //隐藏邮箱给前端
            myPhone = myPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            resultMap.put("success", true);
            resultMap.put("message", myPhone);
            return resultMap;
        }else {
            resultMap.put("success", false);
            resultMap.put("message", "验证码错误，失效或手机已经被注册!");
            return resultMap;
        }
    }

    //验证个人实名信息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/testCard")
    public Map<String, Object> testCard(@RequestParam("resetName")String resetName,@RequestParam("resetCode")String resetCode,
                                        @RequestParam("username")String username){
        Map<String, Object> resultMap = new HashMap<>();
        //判定是否存在个人信息的对应
        int result=resetService.checkPerson(resetName, resetCode, username);
        if (result>0){
            //允许验证的话返还客户端
            //先判定一天的限期是否存在
            int time=resetService.timeCheck(resetName,username);
            if (time==0){
                resultMap.put("success", false);
                resultMap.put("message", "一天只能验证一次,请明天再试!");
                return resultMap;
            }
            resultMap.put("success", true);
            resultMap.put("message", "验证成功!");
            return resultMap;
        }else {
            resultMap.put("success", false);
            resultMap.put("message", "验证失败!");
            return resultMap;
        }
    }

    //实名验证的新手机验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetNewPhone")
    public Map<String, Object> resetNewPhone(@RequestParam("resetNewPhone") String resetNewPhone){
        Map<String, Object> resultMap = new HashMap<>();
        int result = phoneService.registerPhoneCode(resetNewPhone);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "手机验证码已发送" : "手机已被注册");
        return resultMap;
    }

    //确认重置手机并且返回加密的手机给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetNewPhoneUpdate")
    public Map<String, Object> resetNewPhoneUpdate(@RequestParam("resetNewPhone") String resetNewPhone, @RequestParam("resetPhoneCode") String resetPhoneCode,
                                                   @RequestParam("username")String username){
        Map<String, Object> resultMap = new HashMap<>();
        int result=phoneService.registerPhone(resetNewPhone, resetPhoneCode, username);
        if (result>0){
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

    //获取手机和实名认证的标志返还给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetShow")
    public Map<String, Object> resetShow(@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //获取手机和实名认证的标志
        String phone=phoneService.getPhoneByUsername(username);
        String card=phoneService.getNameCardByUsername(username);
        //1.均不为空值.
        if (phone!=null && card!=null){
            resultMap.put("sort",1);
        }
        //2.身份证为空值.
        else if (phone!=null && card==null){
            resultMap.put("sort",2);
        }
        //3.手机为null而身份证不为null(不可能实现)
        else if (phone==null && card!=null){
            resultMap.put("sort",3);
        }
        //4.均为空值.
        else {
            resultMap.put("sort",4);
        }
        return resultMap;
    }
}