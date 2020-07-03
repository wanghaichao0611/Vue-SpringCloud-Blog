package com.whc.cloud.ServiceImpl;

import com.whc.cloud.Mapper.UserSecurityMapper;
import com.whc.cloud.Service.CodeService;
import com.whc.cloud.Service.ResetService;
import com.whc.cloud.User.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class ResetServiceImpl implements ResetService {

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

    //手机重置邮箱的验证码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int resetEmail(String ssPhone, String username) {
        //查询phone和username是否绑定存在
        String usernameByPhone=userSecurityMapper.selectUsernameByPhone(ssPhone);
        if (username.equals(usernameByPhone)){
            //如果存在的话就发送手机验证码
            codeService.sendPhoneCode(ssPhone);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //确认邮箱是否可以被重置
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkEmail(String ssPhone, String ssYzm, String ssEmail, String username) {
        //先查询手机和验证码是否双向绑定(防止发送失败情况产生多余的验证码)
        Jedis jedis = new Jedis("localhost", 6379);
        if (ssPhone.equals(jedis.get(ssYzm)) && ssYzm.equals(jedis.get(ssPhone))){
            //如果在Redis中存在双向绑定就更新手机号，并且邮箱没有被注册
            List<String> realEmail=userSecurityMapper.selectUsernameByEmail(ssEmail);
            if (realEmail.isEmpty()){
                //如果邮箱查询不到,则更新邮箱
                userSecurityMapper.updateEmailByUsername(ssEmail,username);
                //删除Redis中双向绑定的验证码
                jedis.del(ssPhone);
                jedis.del(ssYzm);
                return SUCCESS;
            }else {
                return FAILED;
            }
        }else {
            return FAILED;
        }
    }

    //更换新的手机号(判定手机号没有被注册)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int updatePhone(String oldPhone, String phoneChangeYzm, String newPhone, String username) {
        //先查询手机和验证码是否双向绑定(防止发送失败情况产生多余的验证码)
        Jedis jedis = new Jedis("localhost", 6379);
        if (oldPhone.equals(jedis.get(phoneChangeYzm)) && phoneChangeYzm.equals(jedis.get(oldPhone))){
            //如果双向绑定验证成功,则判定新手机号
            //判定新手机号是否存在被其他人注册
            List<Long> realPhone=userSecurityMapper.selectIdByPhone(newPhone);
            if (realPhone.isEmpty()){
                //如果没有注册,则更新新的手机号
                userSecurityMapper.updatePhone(newPhone,username);
                //删除Redis中双向绑定的验证码
                jedis.del(oldPhone);
                jedis.del(phoneChangeYzm);
                return SUCCESS;
            }{
                return FAILED;
            }
        }else {
            return FAILED;
        }
    }

    //验证个人信息是否存在
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkPerson(String resetName, String resetCode, String username) {
       //判定是否存在个人信息正确
        if (username.equals(userSecurityMapper.selectUsernameByRealnameAndCardnumber(resetName,resetCode))){
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //控制一天的限时验证
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int timeCheck(String resetName, String username) {
        Jedis jedis = new Jedis("localhost", 6379);
        //姓名和username存在练习话放回不可用的凭证
        if (username.equals(jedis.get(resetName))){
            return FAILED;
        }else {
            //返回成功,并且设置一天的限时
            jedis.set(resetName,username);
            jedis.expire(resetName,86400);
            return SUCCESS;
        }
    }
}
