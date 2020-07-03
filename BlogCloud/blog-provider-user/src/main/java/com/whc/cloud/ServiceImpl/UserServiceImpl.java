package com.whc.cloud.ServiceImpl;

import com.whc.cloud.Mapper.UserIfMapper;
import com.whc.cloud.Mapper.UserMapper;
import com.whc.cloud.Service.UserService;
import com.whc.cloud.User.User;
import com.whc.cloud.User.UserIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper=null;
    @Autowired
    private UserIfMapper userIfMapper=null;
    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //登录验证
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int login(String username, String password) {
        //查询信息
        List<User> user=userMapper.selectBySnameAndSpassword(username,password);
        if (user.isEmpty()){
            return FAILED;
        }else {
            return SUCCESS;
        }
    }

    //注册账号
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int register(String newUsername,String newPassword) {
        //查询是否已经被注册，只含有账号名的验证
        List<User> user = userMapper.selectSname(newUsername, newPassword);
        UserIf userIf=new UserIf();
        userIf.setUsername(newUsername);
        if (user.isEmpty()) {
            //获取插入表的主键ID
            User user1=new User();
            user1.setUsername(newUsername);
            user1.setPassword(newPassword);
            //插入信息
            userMapper.insert(user1);
            if (user1.getId()!=null) {
                //把注册的用户名插入信息关联表中
                userIf.setIfId(user1.getId());
                //获取当前系统创建信息表的时间
                Timestamp createTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
                userIf.setCreatetime(createTime);
                userIf.setUpdatetime(createTime);
                userIfMapper.insert(userIf);
                return SUCCESS;
            } else {
                return FAILED;
            }
        }else {
            return FAILED;
        }
    }

    //修改密码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int xiugaimima(String usernameXGMM,String oldPassword,String newPassword) {
        //先查询是否存在账号
        List<User> userOne = userMapper.selectSname(usernameXGMM, oldPassword);
        //如果不存在账号则返回失败
        if (userOne.isEmpty()) {
            return FAILED;
        } else {
            //否则验证账号存在再查询密码是否相同
            List<User> userTwo = userMapper.selectBySnameAndSpassword(usernameXGMM, oldPassword);
            //如果密码和账号都相同，则改写把新密码覆盖旧密码
            if (userTwo.isEmpty()){
                return FAILED;
            }else {
                //覆盖方法
                userMapper.updateSpassword(usernameXGMM,oldPassword,newPassword);
                return SUCCESS;
            }
        }
    }
}
