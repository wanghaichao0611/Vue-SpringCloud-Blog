package com.whc.cloud.ServiceImpl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.whc.cloud.Mapper.UserSecurityMapper;
import com.whc.cloud.Service.CodeService;
import com.whc.cloud.User.UserSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class CodeServiceImpl implements CodeService {

    //发送邮件的邮箱
    private static  String FORM="QQ发送邮箱";

    //邮箱的主题(验证码)
    private static  String SUBJECT="邮箱的验证码";

    //邮箱的主题(通知)
    private static  String MSG="邮箱的验证码";

    //邮件的验证码
    private static  String EMAIL_CODES="0123456789";

    //控制台
    private static Logger LOGGER = LoggerFactory.getLogger(CodeServiceImpl.class);

    //安全的Mapper
    @Autowired
    UserSecurityMapper userSecurityMapper;

    //发送邮件的接口
    @Autowired
    JavaMailSender javaMailSender;

    //自动生成的验证码,验证码的位数可以自己设定。
    public static String generateVerifyCode(int verifySize, String sources){
        if(sources == null || sources.length() == 0){
            sources = EMAIL_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for(int i = 0; i < verifySize; i++){
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    //发送邮箱的验证码的按钮服务
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void sendEmailCode(String email) {
        try {
            //生成邮箱的验证码
            String emailYzm = generateVerifyCode(6, EMAIL_CODES);

            //From-to,主题和信息.
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(FORM);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject(SUBJECT);
            simpleMailMessage.setText("你的邮箱验证码是: " + emailYzm + "本次验证码会在10分钟后失效，请立马使用。");
            //发送邮箱验证码
            javaMailSender.send(simpleMailMessage);

            //开启Redis存入email和yzm
            Jedis jedisEmail = new Jedis("localhost", 6379);

            //设置邮箱(key)-验证码(value)的绑定，秒为单位，存在时间为10分钟。
            jedisEmail.set(email, emailYzm);
            jedisEmail.expire(email, 600);

            //设置验证码(key)-邮箱(value)的绑定，秒为单位，存在时间为10分钟。(双向绑定可以判断失败存入的验证码,双向保险)
            jedisEmail.set(emailYzm, email);
            jedisEmail.expire(emailYzm, 600);
        }catch (Exception e){
            LOGGER.info("发送邮箱又出错了");
        }

    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void sendPhoneCode(String phone) {


        //生成手机的验证码
        String phoneYzm= generateVerifyCode(6, EMAIL_CODES);

        //阿里云发送短信的API
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "阿里云操作账号",
                "阿里云操作密码");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "博客的设计与实现的验证码");
        request.putQueryParameter("TemplateCode", "模板Code");
        request.putQueryParameter("TemplateParam", "{\"codeab\":\""+phoneYzm+"\"}");

        //发送注册手机的验证码
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        //开启Redis存入phone和yzm
        Jedis jedisPhone = new Jedis("localhost", 6379);

        //设置手机(key)-验证码(value)的绑定，秒为单位，存在时间为10分钟。
        jedisPhone.set(phone,phoneYzm);
        jedisPhone.expire(phone,600);

        //设置验证码(key)-手机(value)的绑定,秒为单位，存在时间为10分钟。(双向绑定可以判断失败存入的验证码,双向保险)
        jedisPhone.set(phoneYzm,phone);
        jedisPhone.expire(phoneYzm,600);

    }

    //发送邮件的通知
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void sendEmailMsg(Long id, String msg) {

        //根据Id查询用户名与邮箱
        UserSecurity userSecurity=userSecurityMapper.selectAllByUserId(id);
        if (userSecurity.getEmail()==null || userSecurity.getEmail().equals("")){
            LOGGER.info("未完成注册邮箱!");
            return;
        }
        try {
            //开通时间，秒数差可忽略
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());

            //From-to,主题和信息.
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(FORM);
            simpleMailMessage.setTo(userSecurity.getEmail());
            simpleMailMessage.setSubject(MSG);
            simpleMailMessage.setText("尊敬的客户您好，您的博客账号为" + userSecurity.getUsername() + "的用户，于"
                    + time + "时，" + msg + "，祝您的生活愈快!");

            LOGGER.info(id + "已完成发送邮箱提醒!");

            //邮箱发送信息
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            LOGGER.info("发送邮箱又出错了");
        }
    }
}
