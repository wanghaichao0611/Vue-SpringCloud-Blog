package com.Controller;

import com.Service.UsernameToken;
import com.feign.UserSecurityFeign;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserSecurityFeignController {

    //安全认证的Feign
    @Autowired
    private UserSecurityFeign userSecurityFeign;

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;


    //当其他微服务需要验证码的时候需要用到这个服务提供者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/eachServerCodeFeign")
    @AuthToken
    public Map<String, Object> eachServerCodeFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.eachServerCode(username);
    }

    //其他微服务确认验证码返回给其他微服务的标志
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/checkServerCodeFeign")
    @AuthToken
    public Map<String,Object> checkServerCodeFeign(@RequestParam("phoneCode") String phoneCode){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.checkServerCode(phoneCode,username);
    }


    //查询邮箱返还给后端(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getEmailButton")
    @AuthToken
    public Map<String, Object> getEmailButton() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.getEmail(username);
    }

    //查询手机号返还给后端(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getPhoneButton")
    @AuthToken
    public Map<String, Object> getPhoneButton() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.getPhone(username);
    }

    //获得绑定邮箱的验证码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailButtonRequire")
    @AuthToken
    public Map<String, Object> emailButtonRegister(@RequestParam("email") String email) {
        return userSecurityFeign.emailButton(email);
    }

    //判断绑定邮箱的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailButtonRegister")
    @AuthToken
    public Map<String, Object> emailButtonRegister(@RequestParam("email") String email, @RequestParam("emailYzm") String emailYzm) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.emailRegister(email, emailYzm, username);
    }

    //获得邮箱重置密码按钮的验证码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailButtonReset")
    @AuthToken
    public Map<String, Object> emailButtonReset(@RequestParam("chEmail") String chEmail) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.emailReset(username, chEmail);
    }

    //判断绑定邮箱的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/emailButtonResetPassword")
    @AuthToken
    public Map<String, Object> emailButtonResetPassword(@RequestParam("chEmail") String chEmail, @RequestParam("passwordYzm") String passwordYzm, @RequestParam("chPassword") String chPassword) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.emailResetPassword(chEmail, passwordYzm, chPassword, username);
    }

    //获得手机重置密码的验证码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneResetButton")
    @AuthToken
    public Map<String, Object> phoneResetButton(@RequestParam("phoneReset") String phoneReset){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.phoneResetCardCode(phoneReset,username);
    }

    //判定手机重置密码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneButtonResetPassword")
    @AuthToken
    public Map<String, Object> phoneButtonResetPassword(@RequestParam("phoneReset") String phoneReset, @RequestParam("phoneResetCode") String phoneResetCode,
                                                @RequestParam("phonePassword") String phonePassword){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.phoneResetUpdate(phoneReset,phoneResetCode,phonePassword,username);
    }

    //实名重置密码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/cardResetPassword")
    @AuthToken
    public Map<String, Object> cardResetPassword(@RequestParam("cardName")String cardName,@RequestParam("cardCode")String cardCode,
                                         @RequestParam("cardPassword")String cardPassword){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.cardReset(cardName,cardCode,cardPassword,username);
    }

    //获得绑定手机的验证码的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneCodeButton")
    @AuthToken
    public Map<String, Object> phoneCodeButton(@RequestParam("phone") String phone) {
        return userSecurityFeign.phoneCode(phone);
    }

    //确认绑定手机的服务消费者(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneRegisterButton")
    @AuthToken
    public Map<String, Object> phoneRegisterButton(@RequestParam("phone") String phone, @RequestParam("phoneYzm") String phoneYzm) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.phoneRegister(phone, phoneYzm, username);
    }

    //实名认证的手机验证码(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneCardCodeButton")
    @AuthToken
    public Map<String, Object> phoneCardCodeButton(@RequestParam("phoneSFRZ") String phoneSFRZ) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.phoneCardCode(phoneSFRZ, username);
    }

    //实名认证的确认更新(Token)
    //确认实名认证
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/phoneCardRegisterButton")
    @AuthToken
    public Map<String, Object> phoneCardRegisterButton(@RequestParam("phoneSFRZ") String phoneSFRZ, @RequestParam("phoneCode") String phoneCode,
                                                       @RequestParam("realnameSFRZ") String realnameSFRZ, @RequestParam("cardNumber") String cardNumber) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.phoneCardRegister(phoneSFRZ, phoneCode, realnameSFRZ, cardNumber, username);
    }

    //前端获得实名认证的信息(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getNameCardButton")
    @AuthToken
    public Map<String, Object> getNameCardButton() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.getNameCard(username);
    }

    //前端获得学校的信息(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSchoolButton")
    @AuthToken
    public Map<String, Object> getSchoolButton(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.getSchool(username);
    }

    //校园认证的注册消息(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/registerSchoolButton")
    @AuthToken
    public Map<String, Object> registerSchoolButton(@RequestParam("realnameXYRZ") String realnameXYRZ,@RequestParam("schoolNumber") String schoolNumber,
                                              @RequestParam("schoolName") String schoolName){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.registerSchool(realnameXYRZ,schoolNumber,schoolName,username);
    }

    //前端获取整个安全验证(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSecurityAllFeign")
    @AuthToken
    public Map<String, Object> getSecurityAllFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.getSecurityAll(username);
    }

    //手机重置邮箱的验证码(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetEmailButton")
    @AuthToken
    public Map<String, Object> resetEmailButton(@RequestParam("ssPhone") String ssPhone){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.resetEmail(ssPhone,username);
    }

    //手机重置邮箱的更新(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetEmailFeign")
    @AuthToken
    public Map<String, Object> resetEmailFeign(@RequestParam("ssPhone") String ssPhone, @RequestParam("ssYzm") String ssYzm,
                                                @RequestParam("ssEmail") String ssEmail){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.resetEmailUpdate(ssPhone,ssYzm,ssEmail,username);
    }

    //旧手机号的验证码(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/changePhoneButton")
    @AuthToken
    public Map<String, Object> changePhoneButton(@RequestParam("oldPhone") String oldPhone){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.changePhone(oldPhone,username);
    }

    //新手机号的更新(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/changePhoneFeign")
    @AuthToken
    public Map<String, Object> changePhoneUpdate(@RequestParam("oldPhone") String oldPhone, @RequestParam("phoneChangeYzm") String phoneChangeYzm,
                                                 @RequestParam("newPhone") String newPhone){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.changePhoneUpdate(oldPhone,phoneChangeYzm,newPhone,username);
    }

    //验证个人实名信息(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/testCardButton")
    @AuthToken
    public Map<String, Object> testCardButton(@RequestParam("resetName")String resetName,@RequestParam("resetCode")String resetCode){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.testCard(resetName,resetCode,username);
    }

    //实名重置手机的验证码(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetNewPhoneButton")
    @AuthToken
    public Map<String, Object> resetNewPhoneButton(@RequestParam("resetNewPhone") String resetNewPhone){
        return userSecurityFeign.resetNewPhone(resetNewPhone);
    }

    //实名重置手机的更新(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetNewPhoneFeign")
    @AuthToken
    public Map<String, Object> resetNewPhoneFeign(@RequestParam("resetNewPhone") String resetNewPhone, @RequestParam("resetPhoneCode") String resetPhoneCode){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.resetNewPhoneUpdate(resetNewPhone,resetPhoneCode,username);
    }

    //前端获取整个重置页面的标志(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetShowFeign")
    @AuthToken
    public Map<String, Object> resetShowFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.resetShow(username);
    }

    //前端登录时获得整个安全表的验证
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSecurityLoginFeign")
    @AuthToken
    public Map<String, Object> getSecurityLoginFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        return userSecurityFeign.getSecurityLogin(username);
    }
}