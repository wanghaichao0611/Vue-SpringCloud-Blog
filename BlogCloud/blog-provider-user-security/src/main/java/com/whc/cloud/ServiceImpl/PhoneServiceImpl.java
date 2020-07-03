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
import com.whc.cloud.Service.PhoneService;
import com.whc.cloud.User.NameCard;
import com.whc.cloud.User.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //Mapper接口
    @Autowired
    UserSecurityMapper userSecurityMapper;

    //发送验证码的接口
    @Autowired
    CodeService codeService;

    //当其他微服务需要验证码的时候需要用到这个服务提供者
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int sendPhoneCodeForEachServer(String username) {
        //根据username查询手机号
        String securityPhone=userSecurityMapper.selectPhoneByUsername(username);
        if (securityPhone!=null) {
            //发送验证码
            codeService.sendPhoneCode(securityPhone);
            return SUCCESS;
        }
        return FAILED;
    }

    //当其他微服务需要确认验证码的时候需要用到这个服务提供者(暂时支付微服务用的到)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkServerPhone(String payCode, String username) {
        //根据username查询手机号
        String securityPhone=userSecurityMapper.selectPhoneByUsername(username);
        //先查询手机和验证码是否双向绑定(防止发送失败情况产生多余的验证码)
        Jedis jedis = new Jedis("localhost", 6379);
        if (payCode.equals(jedis.get(securityPhone)) && securityPhone.equals(jedis.get(payCode))){
            //验证成功的话删除验证码和双向绑定
            jedis.del(payCode);
            jedis.del(securityPhone);
            //返回成功标志1
            return SUCCESS;
        }
        return FAILED;
    }

    //返还前端手机给前端
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getPhoneByUsername(String username) {
        //查询手机号
        String myPhone=userSecurityMapper.selectPhoneByUsername(username);
        return myPhone;
    }

    //注册手机的验证码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int registerPhoneCode(String phone) {
        //先查询手机是否被注册
        List<UserSecurity> phoneRegister = userSecurityMapper.selectAllByPhone(phone);
        if (phoneRegister.isEmpty()) {
            //如果手机没有被注册,则发送验证码
            codeService.sendPhoneCode(phone);
            return SUCCESS;
        } else {
            return FAILED;
        }
    }

    //确认绑定手机
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int registerPhone(String phone, String phoneYzm,String username) {
        //先查询手机和验证码是否双向绑定(防止发送失败情况产生多余的验证码)
        Jedis jedis = new Jedis("localhost", 6379);
        if (phone.equals(jedis.get(phoneYzm)) && phoneYzm.equals(jedis.get(phone))){
            //如果在Redis中存在双向绑定就更新手机号
            userSecurityMapper.updatePhoneByUsername(phone,username);
            //删除Redis中双向绑定的验证码
            jedis.del(phone);
            jedis.del(phoneYzm);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }


    //username+phone确定成对存在才发送验证码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int registerCardCode(String phoneSFRZ, String username) {
        //查询是否两者同时存在
        List<UserSecurity> cardCode=userSecurityMapper.selectAllByUsernameAndPhone(phoneSFRZ,username);
        if (!cardCode.isEmpty()){
            //不为空的时候发送手机验证码
            codeService.sendPhoneCode(phoneSFRZ);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //根据phone+code验证是正确，若正确，就把姓名和身份证写入(Token的username作为指定位置)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int registerCard(String phoneSFRZ, String phoneCode, String realnameSFRZ, String cardNumber, String username) {
        //查询身份证是否已经被注册
        List<UserSecurity> card=userSecurityMapper.selectAllByCardnumber(cardNumber);
        //如果身份证没有被注册
        if (card.isEmpty()) {
            //如果验证码和手机绑定不正确，就返回失败
            Jedis jedis = new Jedis("localhost", 6379);
            if (phoneSFRZ.equals(jedis.get(phoneCode)) && phoneCode.equals(jedis.get(phoneSFRZ))) {
                //如果存在则需要更新写入的姓名和身份证
                userSecurityMapper.updateRealnameAndCardnumberByUsername(realnameSFRZ, cardNumber, username);
                //删除有效验证码
                jedis.del(phoneSFRZ);
                jedis.del(phoneCode);
                return SUCCESS;
            } else {
                return FAILED;
            }
        }else {
            return FAILED;
        }
    }

    //获取实名认证的真实姓名返还给前端
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public String getRealNameByUsername(String username) {
        //根据username查询的真实姓名存在返回给前端
        String realName=userSecurityMapper.selectRealnameByUsername(username);
        return realName;
    }

    //获取实名认证的身份证返还给前端
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getNameCardByUsername(String username) {
        //根据username查询身份证号码的存在返回给前端
        String realCard=userSecurityMapper.selectCardnumberByUsername(username);
        return realCard;
    }

    //获得手机的重置验证码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int phoneReset(String phoneReset, String username) {
        //查询是否两者同时存在
        List<UserSecurity> cardCode=userSecurityMapper.selectAllByPhoneAndUsername(phoneReset,username);
        if (!cardCode.isEmpty()){
            //不为空的时候发送手机验证码
            codeService.sendPhoneCode(phoneReset);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //确认手机重置密码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int phoneResetPassword(String phoneReset, String phoneResetCode, String phonePassword, String username) {
        Jedis jedis = new Jedis("localhost", 6379);
        //验证成功更新密码
        if (phoneReset.equals(jedis.get(phoneResetCode)) && phoneResetCode.equals(jedis.get(phoneReset))){
            //删除Redis中双向绑定的验证码
            jedis.del(phoneReset);
            jedis.del(phoneResetCode);
            userSecurityMapper.updatePasswordPhone(phonePassword,username);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //实名认证重置密码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int cardResetPassword(String cardName, String cardCode, String cardPassword, String username) {
        //如果可以查询到个人的信息通过username+name+card
        List<UserSecurity> user=userSecurityMapper.selectAllByRealnameAndCardnumber(cardName,cardCode);
        if (!user.isEmpty()){
            //如果找到了符合信息则更新密码
            userSecurityMapper.updatePasswordCard(cardPassword,username);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }
}
