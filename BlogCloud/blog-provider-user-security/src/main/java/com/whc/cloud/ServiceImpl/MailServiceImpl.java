package com.whc.cloud.ServiceImpl;

import com.whc.cloud.Mapper.UserSecurityMapper;
import com.whc.cloud.Service.CodeService;
import com.whc.cloud.Service.MailService;
import com.whc.cloud.User.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {

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

    //返还邮箱给前端判定
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getEmailByUsername(String username) {
        //查询邮箱
        String myEmail =userSecurityMapper.selectEmailByUsername(username);
         return myEmail;
    }

    //发送绑定邮箱的验证码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int sendEmailNumber(String email) {
        //先查询邮箱是否已经被他人注册
        List<UserSecurity> emailRegister = userSecurityMapper.selectAllByEmail(email);
        if (emailRegister.isEmpty()) {
            codeService.sendEmailCode(email);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //判断验证码的失效时间以及更新邮箱
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int checkEmail(String email, String emailYzm,String username) {
        Jedis jedis = new Jedis("localhost", 6379);
        //如果邮箱和验证码相等(双向绑定可以判断失败存入的验证码,双向保险)
        if (email.equals(jedis.get(emailYzm)) && emailYzm.equals(jedis.get(email))){
            //删除Redis中双向绑定的验证码
            jedis.del(email);
            jedis.del(emailYzm);
            userSecurityMapper.updateEmail(email,username);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //重置密码的验证码(判定username)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int updateEmail(String username,String chEmail) {
        //如果Token传来的username与email相对应就可以获得验证码
        List<UserSecurity> userSecurity=userSecurityMapper.selectAllByUsernameAndEmail(username,chEmail);
        if (!userSecurity.isEmpty()){
            codeService.sendEmailCode(chEmail);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //判定验证码和邮箱的验证最后更新密码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int updatePassword(String chEmail, String passwordYzm, String chPassword, String username) {
        Jedis jedis = new Jedis("localhost", 6379);
        //验证成功更新密码
        if (chEmail.equals(jedis.get(passwordYzm)) && passwordYzm.equals(jedis.get(chEmail))){
            //删除Redis中双向绑定的验证码
            jedis.del(chEmail);
            jedis.del(passwordYzm);
            userSecurityMapper.updatePassword(chPassword,username);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }
}
