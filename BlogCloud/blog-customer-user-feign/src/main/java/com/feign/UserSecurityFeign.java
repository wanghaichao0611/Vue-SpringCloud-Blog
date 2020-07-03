package com.feign;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//feign对应Security微服务
@FeignClient(name = "blog-provider-user-security",fallbackFactory = SecurityFeignFallback.class)
public interface UserSecurityFeign {

    //当其他微服务需要验证码的时候需要用到这个服务提供者(Token)
    @RequestMapping(value = "/eachServerCode", method = RequestMethod.POST)
    public Map<String, Object> eachServerCode(@RequestParam("username") String username);

    //其他微服务确认验证码返回给其他微服务的标志
    @RequestMapping(value = "/checkServerCode", method = RequestMethod.POST)
    public Map<String,Object> checkServerCode(@RequestParam("phoneCode") String phoneCode,@RequestParam("username") String username);

        //查询邮箱返还给前端(Token)
    @RequestMapping(value = "/getEmail", method = RequestMethod.POST)
    public Map<String, Object> getEmail(@RequestParam("username") String username);

    //查询手机号返还给前端(Token)
    @RequestMapping(value = "/getPhone", method = RequestMethod.POST)
    public Map<String, Object> getPhone(@RequestParam("username") String username);

    //获得验证码的按钮的Button(Token)
    @RequestMapping(value = "/emailButton", method = RequestMethod.POST)
    public Map<String, Object> emailButton(@RequestParam("email") String email);

    //判断邮箱绑定(Token)
    @RequestMapping(value = "/emailRegister", method = RequestMethod.POST)
    public Map<String,Object> emailRegister(@RequestParam("email")String email,@RequestParam("emailYzm") String emailYzm,@RequestParam("username")String username);

    //邮箱重置密码的Button(Token)
    @RequestMapping(value = "/emailReset", method = RequestMethod.POST)
    public Map<String, Object> emailReset(@RequestParam("username") String username,@RequestParam("chEmail") String chEmail);

    //手机重置密码的Button(Token)
    @RequestMapping(value = "/phoneResetCardCode", method = RequestMethod.POST)
    public Map<String, Object> phoneResetCardCode(@RequestParam("phoneReset") String phoneReset, @RequestParam("username") String username);

    //手机重置密码的更新(Token)
    @RequestMapping(value = "/phoneResetUpdate", method = RequestMethod.POST)
    public Map<String, Object> phoneResetUpdate(@RequestParam("phoneReset") String phoneReset, @RequestParam("phoneResetCode") String phoneResetCode,
                                                  @RequestParam("phonePassword") String phonePassword,@RequestParam("username") String username);

    //实名重置密码的更新(Token)
    @RequestMapping(value = "/cardReset", method = RequestMethod.POST)
    public Map<String, Object> cardReset(@RequestParam("cardName")String cardName,@RequestParam("cardCode")String cardCode,
                                         @RequestParam("cardPassword")String cardPassword,@RequestParam("username") String username);

    //邮箱重置密码的更新(Token)
    @RequestMapping(value = "/emailResetPassword", method = RequestMethod.POST)
    public Map<String, Object> emailResetPassword(@RequestParam("chEmail") String chEmail, @RequestParam("passwordYzm") String emailYzm,
                                             @RequestParam("chPassword") String chPassword, @RequestParam("username") String username);

    //绑定手的验证码(Token)
    @RequestMapping(value = "/phoneCode", method = RequestMethod.POST)
    public Map<String, Object> phoneCode(@RequestParam("phone") String phone);

    //绑定手机(Token)
    @RequestMapping(value = "/phoneRegister", method = RequestMethod.POST)
    public Map<String, Object> phoneRegister(@RequestParam("phone") String phone,@RequestParam("phoneYzm") String phoneYzm,@RequestParam("username") String username);

    //实名认证的验证码(Token)
    @RequestMapping(value = "/phoneCardCode", method = RequestMethod.POST)
    public Map<String, Object> phoneCardCode(@RequestParam("phoneSFRZ") String phoneSFRZ, @RequestParam("username") String username);

    //确认实名认证(Token)
    @RequestMapping(value = "/phoneCardRegister", method = RequestMethod.POST)
    public Map<String, Object> phoneCardRegister(@RequestParam("phoneSFRZ")String phoneSFRZ,@RequestParam("phoneCode")String phoneCode,
                                                 @RequestParam("realnameSFRZ")String realnameSFRZ,@RequestParam("cardNumber")String cardNumber,
                                                 @RequestParam("username")String username);

    //前端获取实名认证的信息
    @RequestMapping(value = "/getNameCard", method = RequestMethod.POST)
    public Map<String, Object> getNameCard(@RequestParam("username") String username);

    //前端获得校园认证的信息
    @RequestMapping(value = "/getSchool", method = RequestMethod.POST)
    public Map<String, Object> getSchool(@RequestParam("username") String username);

    //注册校园认证的消息
    @RequestMapping(value = "/registerSchool", method = RequestMethod.POST)
    public Map<String, Object> registerSchool(@RequestParam("realnameXYRZ") String realnameXYRZ,@RequestParam("schoolNumber") String schoolNumber,
                                         @RequestParam("schoolName") String schoolName,@RequestParam("username") String username);

    //前端获得整个安全验证
    @RequestMapping(value = "/getSecurityAll", method = RequestMethod.POST)
    public Map<String, Object> getSecurityAll(@RequestParam("username") String username);

    //手机重置邮箱的验证码
    @RequestMapping(value = "/resetEmail", method = RequestMethod.POST)
    public Map<String, Object> resetEmail(@RequestParam("ssPhone") String ssPhone, @RequestParam("username") String username);

    //手机重置邮箱的更新
    @RequestMapping(value = "/resetEmailUpdate", method = RequestMethod.POST)
    public Map<String, Object> resetEmailUpdate(@RequestParam("ssPhone") String ssPhone, @RequestParam("ssYzm") String ssYzm,
                                                @RequestParam("ssEmail") String ssEmail, @RequestParam("username") String username);

    //旧手机号的Button验证码
    @RequestMapping(value = "/changePhone", method = RequestMethod.POST)
    public Map<String, Object> changePhone(@RequestParam("oldPhone") String oldPhone, @RequestParam("username") String username);


    //更换新的手机号
    @RequestMapping(value = "/changePhoneUpdate", method = RequestMethod.POST)
    public Map<String, Object> changePhoneUpdate(@RequestParam("oldPhone") String oldPhone, @RequestParam("phoneChangeYzm") String phoneChangeYzm,
                                                 @RequestParam("newPhone") String newPhone,@RequestParam("username") String username);

    //验证个人实名的信息
    @RequestMapping(value = "/testCard", method = RequestMethod.POST)
    public Map<String, Object> testCard(@RequestParam("resetName")String resetName,@RequestParam("resetCode")String resetCode,
                                        @RequestParam("username")String username);

    //实名重置新手机发短信服务
    @RequestMapping(value = "/resetNewPhone", method = RequestMethod.POST)
    public Map<String, Object> resetNewPhone(@RequestParam("resetNewPhone") String resetNewPhone);

    //实名确认重置手机并返还给前端
    @RequestMapping(value = "/resetNewPhoneUpdate", method = RequestMethod.POST)
    public Map<String, Object> resetNewPhoneUpdate(@RequestParam("resetNewPhone") String resetNewPhone, @RequestParam("resetPhoneCode") String resetPhoneCode,
                                                   @RequestParam("username")String username);

    //前端获取重置的渲染
    @RequestMapping(value = "/resetShow", method = RequestMethod.POST)
    public Map<String, Object> resetShow(@RequestParam("username") String username);

    //登录时获得整个安全表的信息
    @RequestMapping(value = "/getSecurityLogin", method = RequestMethod.POST)
    public Map<String, Object> getSecurityLogin(@RequestParam("username") String username);
    }
@Component
class SecurityFeignFallback implements FallbackFactory<UserSecurityFeign>{

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(SecurityFeignFallback.class);

    @Override
    public UserSecurityFeign create(Throwable throwable) {
        return new UserSecurityFeign() {

            //当其他微服务需要验证码的时候需要用到这个服务提供者
            @Override
            public Map<String, Object> eachServerCode(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //其他微服务确认验证码返回给其他微服务的标志
            @Override
            public Map<String, Object> checkServerCode(String phoneCode, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "网络开了小差,请稍后再试!");
                return feignMap;
            }

            //查询邮箱返还给前端
            @Override
            public Map<String, Object> getEmail(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "您还未完成注册，请完成邮箱注册!");
                return feignMap;
            }

            //查询手机号返还给前端
            @Override
            public Map<String, Object> getPhone(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "您还没完成注册，请完成手机注册!");
                return feignMap;
            }

            //邮箱注册按钮的fallback
            @Override
            public Map<String, Object> emailButton(String email) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "按钮超时，请重新再试!");
                return feignMap;
            }

            //邮箱注册写入的fallback
            @Override
            public Map<String, Object> emailRegister(String email, String emailYzm, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "注册超时，请重新再试!");
                return feignMap;
            }

            //邮箱重置密码的按钮的fallback
            @Override
            public Map<String, Object> emailReset(String username, String chEmail) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "按钮超时，请重新再试!");
                return feignMap;
            }

            //邮箱重置密码更新的fallback
            @Override
            public Map<String, Object> emailResetPassword(String chEmail, String emailYzm, String chPassword, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时，请重新再试!");
                return feignMap;
            }

            //手机重置密码的Button的fallback
            @Override
            public Map<String, Object> phoneResetCardCode(String phoneReset, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时，请重新再试!");
                return feignMap;
            }

            //更新手机确认的信息的fallback
            @Override
            public Map<String, Object> phoneResetUpdate(String phoneReset, String phoneResetCode, String phonePassword, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时，请重新再试!");
                return feignMap;
            }

            //实名重置密码的fallback
            @Override
            public Map<String, Object> cardReset(String cardName, String cardCode, String cardPassword, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时，请重新再试!");
                return feignMap;
            }

            //获取手机验证码的按钮的fallback
            @Override
            public Map<String, Object> phoneCode(String phone) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //确认手机绑定的fallback
            @Override
            public Map<String, Object> phoneRegister(String phone, String phoneYzm, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "绑定超时，请重新再试!");
                return feignMap;
            }

            //实名认证的验证码fallback
            @Override
            public Map<String, Object> phoneCardCode(String phoneSFRZ, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //实名认证的更新fallback
            @Override
            public Map<String, Object> phoneCardRegister(String phoneSFRZ, String phoneCode, String realnameSFRZ, String cardNumber, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "认证超时，请重新再试!");
                return feignMap;
            }

            //前端获得实名认证的信息
            @Override
            public Map<String, Object> getNameCard(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "您还为完成实名注册，请完成实名注册!");
                return feignMap;
            }

            //前端获得校园认证的信息
            @Override
            public Map<String, Object> getSchool(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "请先完成实名认证再完成校园认证!");
                return feignMap;
            }

            //注册校园认证的消息
            @Override
            public Map<String, Object> registerSchool(String realnameXYRZ, String schoolNumber, String school, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "认证超时，请重新再试!");
                return feignMap;
            }

            //前端获取整个安全验证
            @Override
            public Map<String, Object> getSecurityAll(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //手机重置验证码的fallback
            @Override
            public Map<String, Object> resetEmail(String ssPhone, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //手机重置密码的fallback
            @Override
            public Map<String, Object> resetEmailUpdate(String ssPhone, String ssYzm, String ssEmail, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "重置超时，请重新再试!");
                return feignMap;
            }

            //旧手机号的验证码的fallback
            @Override
            public Map<String, Object> changePhone(String oldPhone, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //更换新的手机号的fallback
            @Override
            public Map<String, Object> changePhoneUpdate(String oldPhone, String phoneChangeYzm, String newPhone, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "更新超时，请重新再试!");
                return feignMap;
            }

            //验证个人的实名信息的fallback
            @Override
            public Map<String, Object> testCard(String resetName, String resetCode, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "验证超时，请重新再试!");
                return feignMap;
            }

            //实名重置新手机发短信服务的fallback
            @Override
            public Map<String, Object> resetNewPhone(String resetNewPhone) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //实名重置手机的fallback
            @Override
            public Map<String, Object> resetNewPhoneUpdate(String resetNewPhone, String resetPhoneCode, String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "重置超时，请重新再试!");
                return feignMap;
            }

            //前端获取重置的返还的标志的fallback
            @Override
            public Map<String, Object> resetShow(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }

            //登录时获得整个安全表的信息的fallback
            @Override
            public Map<String, Object> getSecurityLogin(String username) {
                SecurityFeignFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时，请重新再试!");
                return feignMap;
            }
        };
    }
}
