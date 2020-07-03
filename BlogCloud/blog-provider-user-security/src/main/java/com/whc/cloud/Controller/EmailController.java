package com.whc.cloud.Controller;

import com.whc.cloud.Service.CodeService;
import com.whc.cloud.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


//和邮箱有关的都在这里面
@RestController
public class EmailController {

    //邮箱的服务
    @Autowired
    MailService mailService;

    @Autowired
    CodeService codeService;

    //判定Token传过来的username查询绑定的邮箱返回给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getEmail")
    public Map<String, Object> getEmail(@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        String myEmail = mailService.getEmailByUsername(username);
        if (myEmail!=null) {
            //隐藏邮箱给前端
            myEmail=myEmail.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
            resultMap.put("success", true);
            resultMap.put("message", myEmail);
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "您的邮箱还未完成绑定,请完成绑定!");
            return resultMap;
        }
    }

    //绑定邮箱按钮验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailButton")
    public Map<String, Object> emailButton(@RequestParam("email") String email) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = mailService.sendEmailNumber(email);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "邮箱验证码已发送" : "邮箱已经被注册");
        return resultMap;
    }

    //邮箱传过来邮箱和验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailRegister")
    public Map<String, Object> emailRegister(@RequestParam("email") String email, @RequestParam("emailYzm") String emailYzm, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        //如果验证码和邮箱从Redis可以相互确认就把email插入(双向绑定可以判断失败存入的验证码,双向保险)
        if (mailService.checkEmail(email,emailYzm,username) > 0) {
            //根据username+返还给前端保存的加密邮箱
           String myEmail=mailService.getEmailByUsername(username);
            //隐藏邮箱给前端
            myEmail=myEmail.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
            resultMap.put("success", true);
            resultMap.put("message", myEmail);
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "验证码错误,失效或邮箱有改动");
            return resultMap;

        }
    }

    //重置密码按钮的验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailReset")
    public Map<String, Object> emailReset(@RequestParam("username") String username, @RequestParam("chEmail") String chEmail) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询username和chEmail验证是否正确
        int result = mailService.updateEmail(username,chEmail);
        boolean flag = result > 0;
        resultMap.put("success", flag);
        resultMap.put("message", flag ? "验证码已发送" : "邮箱未找到");
        return resultMap;
    }

    //邮箱重置密码
    //邮箱传过来邮箱和验证码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailResetPassword")
    public Map<String, Object> emailResetPassword(@RequestParam("chEmail") String chEmail, @RequestParam("passwordYzm") String emailYzm,
                                             @RequestParam("chPassword") String chPassword, @RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        //如果验证码和邮箱从Redis可以相互确认就把chPassword插入(双向绑定可以判断失败存入的验证码,双向保险)
        if (mailService.updatePassword(chEmail,emailYzm,chPassword,username) > 0) {
            resultMap.put("success", true);
            resultMap.put("message", "重置密码成功!");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "验证码错误,失效或邮箱有改动");
            return resultMap;

        }
    }

    //负载均衡发送邮件提示信息
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailMsg")
    public void emailMsg(@RequestParam("id")Long id,@RequestParam("msg")String msg){
        codeService.sendEmailMsg(id, msg);
    }
}