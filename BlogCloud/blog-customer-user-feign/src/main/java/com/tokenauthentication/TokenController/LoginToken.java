package com.tokenauthentication.TokenController;

import com.Entity.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.requset.IAcsTokenRequest;
import com.response.PhoneAndMember;
import com.tokenauthentication.Address.AddressUtil;
import com.tokenauthentication.annotation.AuthToken;
import com.mapper.UserMapperToken;
import com.tokenauthentication.model.ResponseTemplate;
import com.tokenauthentication.testLogin.TestAfsCheck;
import com.tokenauthentication.TokenUtils.ConstantKit;
import com.tokenauthentication.TokenUtils.Md5TokenGenerator;
import com.tokenauthentication.testLogin.TestAfsCheckRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

//所有的登录都是经过这个TokenController,所以这个服务既是服务消费者也是服务提供者.
@RestController
public class LoginToken {

    //控制台输出
     private Logger logger = LoggerFactory.getLogger(LoginToken.class);

    //MD5算法生成Token码
    @Autowired
    Md5TokenGenerator tokenGenerator;

    //查询账号和密码的Mapper
    @Autowired
    UserMapperToken userMapperToken;


    //注册时候的Token(可以指定注册的有效时间,现在规定是30分钟内完成注册)
    @CrossOrigin(origins = "http://localhost:8088")
    @RequestMapping(value = "/registerToken", method = RequestMethod.POST)
    public ResponseTemplate registerToken(@RequestParam("newUsername")String username,@RequestParam("newPassword") String password,
                                          @RequestBody IAcsTokenRequest iAcsTokenRequest) throws Exception {

        logger.info(iAcsTokenRequest+"");
        TestAfsCheckRegister testAfsCheckRegister=new TestAfsCheckRegister();
        if (testAfsCheckRegister.testRegister(iAcsTokenRequest)==0)
        {
            //若存存在多次验证或者验证失败则返回"testFailed"
            return ResponseTemplate.builder()
                    .code(401)
                    .message("testFailed")
                    .data(false)
                    .build();
        }

        logger.info("username:"+username+"      password:"+password);

        User user = userMapperToken.getUser(username,password);

        logger.info("user:"+user);

        JSONObject result = new JSONObject();
        if (user != null) {

            //生成主键id
            Long id=user.getId();

            Jedis jedis = new Jedis("localhost", 6379);

            //Token的生成
            String token = tokenGenerator.generate(username, password);
            jedis.set(username, token);

            //设置key生存时间，当key过期时，它会被自动删除，时间是秒（完成注册的Token是半个小时）
            jedis.expire(username, 1800);
            jedis.set(token, username);
            jedis.expire(token, 1800);

            //用完关闭
            jedis.close();

            result.put("status", 200);
            result.put("token", token);
            //（防止篡改主键ID所以前端没有获取，业务均以username绑定的ID为准）
            result.put("id", id);
        } else {
            result.put("status", 401);
        }

        return ResponseTemplate.builder()
                .code(200)
                .message("注册成功")
                .data(result)
                .build();

    }

    //登录时候的Token(Token存在的时间看情况而定)
    //所有的登录请求都经这个Controller,这个是服务提供者.
    @CrossOrigin(origins = "http://localhost:8088")
    @RequestMapping(value = "/loginToken", method = RequestMethod.POST)
    public ResponseTemplate loginToken (@RequestParam("username") String username, @RequestParam("password") String password, IAcsTokenRequest iAcsTokenRequest)throws Exception {

        logger.info(iAcsTokenRequest+"");
        TestAfsCheck testAfsCheck=new TestAfsCheck();
        if (testAfsCheck.testLogin(iAcsTokenRequest)==0)
        {
            //若存存在多次验证或者验证失败则返回"testFailed"
            return ResponseTemplate.builder()
                    .code(401)
                    .message("testFailed")
                    .data(false)
                    .build();
        }

        logger.info("username:"+username+"      password:"+password);

        User user = userMapperToken.getUser(username,password);

        logger.info("user:"+user);

        JSONObject result = new JSONObject();
        if (user != null) {

            //生成IP和地理位置,免费的不让用，要花钱使用IPv6地理位置库。
            //String addressAll=AddressUtil.getAddresses("ip="+AddressUtil.getIpAddress(httpServletRequest),"utf-8");
            //logger.info("ip="+AddressUtil.getIpAddress(httpServletRequest));

            //生成主键id
            Long id=user.getId();

            //打开Redis
            Jedis jedis = new Jedis("localhost", 6379);

            //删除之前存在的Token
            String tokenOld=jedis.get(username);
            //控制台检测一下是否存在
            logger.info(tokenOld);
            if (tokenOld!=null){
                jedis.del(tokenOld);
                jedis.del(username);
            }
            //返回给前端的blogName
            String blogName=userMapperToken.selectBlogName(username);
            //返回给前端的外网的url地址
            String userPicture=userMapperToken.selectUrl(id);

            //Token的生成
            String token = tokenGenerator.generate(username, password);

            //设置key生存时间，当key过期时，它会被自动删除，时间是秒
            jedis.set(username, token);
            jedis.expire(username, ConstantKit.TOKEN_EXPIRE_TIME);
            jedis.set(token, username);
            jedis.expire(token, ConstantKit.TOKEN_EXPIRE_TIME);

            //用完关闭
            jedis.close();

            //登录的时候查询是是否为会员以及返回加密的手机号备用（这个地方不用负载均衡了）
            //所有的安全都是提供显示信息，判断业务均以数据库中的数据为准
            PhoneAndMember phoneAndMember=userMapperToken.selectPhoneAndMember(id);
            String phone=phoneAndMember.getPhone();
            Integer memberVip=phoneAndMember.getMemberVip();
            Integer memberSvip=phoneAndMember.getMemberSvip();
            if (memberVip!=null && memberSvip!=null) {
                if ( memberVip==1 || memberSvip==1){
                    result.put("memberSign",1);
                }else {
                    result.put("memberSign",0);
                }
            }
            if (phone!=null && !phone.isEmpty()){
                result.put("myPhone",phoneAndMember.getPhone()
                        .replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }else {
                result.put("myPhone","您的手机还未完成绑定,请完成绑定!");
            }
            result.put("status", 200);
            result.put("token", token);
            //（防止篡改主键ID所以前端没有获取，业务均以username绑定的ID为准）
            result.put("id", id);
            result.put("blogName", blogName);
            result.put("userPicture", userPicture);

        } else {
            result.put("status", 401);
        }

        return ResponseTemplate.builder()
                .code(200)
                .message("登陆成功")
                .data(result)
                .build();

    }

    //测试URL GET证明Token存在连接，测试存在Token可以连接.
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @AuthToken
    public ResponseTemplate test() {

        logger.info("已进入test路径");

        return ResponseTemplate.builder()
                .code(200)
                .message("Success")
                .data("test url")
                .build();
    }

}
