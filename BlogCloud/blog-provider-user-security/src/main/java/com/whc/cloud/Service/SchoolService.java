package com.whc.cloud.Service;

import com.whc.cloud.User.School;
import com.whc.cloud.User.UserSecurity;
import org.springframework.stereotype.Service;

@Service
public interface SchoolService {

    //前端获得realName
    String getRealNameByUsername(String username);

    //前端获取schoolNumber
    String getSchoolNumberByUsername(String username);

    //前端获得school
    String getSchoolByUsername(String username);

    //根据token传过来的username判定前端怎么跳转
    School getSchool(String username);

    //根据Token传过来的username判定姓名成功后插入实名认证信息
    int insertSchool(String realnameXYRZ,String schoolNumber,String schoolName,String username);

    //返还整个实体表给前端
    UserSecurity getSecurityAll(String username);

}
