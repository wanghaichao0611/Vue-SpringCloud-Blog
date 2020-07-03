package com.whc.cloud.ServiceImpl;

import com.whc.cloud.Mapper.UserSecurityMapper;
import com.whc.cloud.Service.SchoolService;
import com.whc.cloud.User.School;
import com.whc.cloud.User.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SchoolServiceImpl implements SchoolService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //校园的认证
    @Autowired
    UserSecurityMapper userSecurityMapper;

    //获取真实的姓名(为feign查询返回实体的时候会直接返回不正确的null参数，直接出发回退机制)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getRealNameByUsername(String username) {
        String realName=userSecurityMapper.selectRealnameByUsername(username);
        return realName;
    }

    //前端获取工号(因为feign查询返回实体的时候会直接返回不正确的null参数，直接出发回退机制)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getSchoolNumberByUsername(String username) {
        String schoolNumber=userSecurityMapper.selectSchoolnumberByUsername(username);
        return schoolNumber;
    }

    //前端获取学校(因为feign返回查询实体的时候会直接返回不正确的null参数，直接出发回退机制)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String getSchoolByUsername(String username) {
        String school=userSecurityMapper.selectSchoolByUsername(username);
        return school;
    }

    //前端判定校园认证
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public School getSchool(String username) {
        School schoolAll=userSecurityMapper.selectSchool(username);
        return schoolAll;
    }

    //完成校园认证的信息
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insertSchool(String realnameXYRZ, String schoolNumber, String schoolName,String username) {

        //如果名字正确插入信息
        if (realnameXYRZ.equals(userSecurityMapper.selectRealnameByUsername(username))){
            //验证不为空时，完成注册.
            userSecurityMapper.updateSchoolByUsername(username,schoolNumber,schoolName);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //返还整个实体表给前端
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public UserSecurity getSecurityAll(String username) {
       UserSecurity userSecurity=userSecurityMapper.selectAllByUsername(username);
       return userSecurity;
    }
}
