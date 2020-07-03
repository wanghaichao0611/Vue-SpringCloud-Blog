package com.whc.cloud.Controller;

import com.whc.cloud.Mapper.UserSecurityMapper;
import com.whc.cloud.Service.SchoolService;
import com.whc.cloud.User.School;
import com.whc.cloud.User.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SchoolController {

    //学校操作的服务
    @Autowired
    SchoolService schoolService;

    @Autowired
    UserSecurityMapper userSecurityMapper;

    //前端判定是否实名认证成功然会返回给前端(Token)
    //判定Token传过来的username查询绑定的学校返回给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSchool")
    public Map<String, Object> getSchool(@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        String mySchoolName=schoolService.getRealNameByUsername(username);
        String mySchoolCard=schoolService.getSchoolNumberByUsername(username);
        String myOwnSchool=schoolService.getSchoolByUsername(username);
        if (mySchoolName !=null && myOwnSchool !=null && mySchoolCard !=null) {
            //如果全部存在的话就返回给前端判定
            mySchoolName=mySchoolName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            mySchoolCard=mySchoolCard.replaceAll("(\\d{1})\\d{5}(\\d{1})", "$1****$2");
            myOwnSchool=myOwnSchool.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            resultMap.put("success", 1);
            resultMap.put("mySchoolName", mySchoolName);
            resultMap.put("mySchoolCard", mySchoolCard);
            resultMap.put("myOwnSchool", myOwnSchool);
            return resultMap;
        } else if (mySchoolName !=null && myOwnSchool==null && mySchoolCard==null){
            //如果完成了实名注册
            mySchoolName=mySchoolName.replaceAll("([\\d\\D]{1})(.*)", "$1**");;
            resultMap.put("success", 2);
            resultMap.put("mySchoolName", mySchoolName);
            resultMap.put("mySchoolCard", "请尽快完成校园认证,享受9折优惠!");
            resultMap.put("myOwnSchool", "请尽快完成校园认证,享受9折优惠!");
            return resultMap;
        }else {
            //没有完成实名注册
            resultMap.put("success", 3);
            resultMap.put("message", "请先实名注册");
            return resultMap;
        }
    }

    //注册校园认证
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/registerSchool")
    public Map<String, Object> registerSchool(@RequestParam("realnameXYRZ") String realnameXYRZ,@RequestParam("schoolNumber") String schoolNumber,
                                         @RequestParam("schoolName") String schoolName,@RequestParam("username") String username) {
        Map<String, Object> resultMap = new HashMap<>();
        //传过来的确认信息进行判断
        if (schoolService.insertSchool(realnameXYRZ,schoolNumber,schoolName,username)>0){
            //加密信息传到前端
            School myRegisterSchool = schoolService.getSchool(username);
            String mySchoolName=myRegisterSchool.getRealname();
            String mySchoolCard=myRegisterSchool.getSchoolnumber();
            String myOwnSchool=myRegisterSchool.getSchool();
            mySchoolName=mySchoolName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            mySchoolCard=mySchoolCard.replaceAll("(\\d{1})\\d{5}(\\d{1})", "$1****$2");
            myOwnSchool=myOwnSchool.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            myRegisterSchool.setRealname(mySchoolName);
            myRegisterSchool.setSchoolnumber(mySchoolCard);
            myRegisterSchool.setSchool(myOwnSchool);
            //返回注册的信息给前端
            resultMap.put("success", true);
            resultMap.put("message", myRegisterSchool);
            return resultMap;
        }else {
            resultMap.put("success", false);
            resultMap.put("message", "姓名,工号和学校不匹配");
            return resultMap;
        }
    }

    //获取整个表的实体返还给前端根据username
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getSecurityLogin")
    public Map<String, Object> getSecurityLogin(@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //查询整个表返还给前端
        UserSecurity userSecurity=schoolService.getSecurityAll(username);
        String myEmail=userSecurity.getEmail();
        if (myEmail!=null){
            myEmail=myEmail.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
            userSecurity.setEmail(myEmail);
        }
        String myPhone=userSecurity.getPhone();
        if (myPhone!=null){
            myPhone=myPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            userSecurity.setPhone(myPhone);
        }
        String myName=userSecurity.getRealname();
        String myCard=userSecurity.getCardnumber();
        if (myName!=null && myCard!=null){
            myName=myName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            myCard=myCard.replaceAll("(\\d{1})\\d{16}(\\d{1})", "$1****$2");
            userSecurity.setRealname(myName);
            userSecurity.setCardnumber(myCard);
        }
        String mySchoolCard=userSecurity.getSchoolnumber();
        String mySchool=userSecurity.getSchool();
        String mySchoolName=userSecurity.getRealname();
        if (mySchool!=null && mySchoolCard!=null && mySchoolName!=null){
            mySchoolName=mySchoolName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            mySchoolCard=mySchoolCard.replaceAll("(\\d{1})\\d{5}(\\d{1})", "$1****$2");
            mySchool=mySchool.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            userSecurity.setSchoolnumber(mySchoolCard);
            userSecurity.setSchool(mySchool);
            userSecurity.setRealname(mySchoolName);
        }
        userSecurity.setUsername("你愁啥???");
        resultMap.put("security",userSecurity);
        return resultMap;
    }

    //负载均衡查询是否完成校园注册
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectSchool")
    public Map<String, Object> selectSchool(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //根据用户Id查询校园注册情况
        String schoolNumber=userSecurityMapper.selectSchoolnumberByUserId(id);
        //判断是否为空值
        if (schoolNumber!=null && !schoolNumber.equals("")){
            resultMap.put("result","success");
        }else {
            resultMap.put("result","failed");
        }
        return resultMap;
    }
}
