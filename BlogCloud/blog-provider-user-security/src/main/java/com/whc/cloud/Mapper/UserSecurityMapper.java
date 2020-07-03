package com.whc.cloud.Mapper;

import com.whc.cloud.User.School;import com.whc.cloud.User.UserSecurity;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface UserSecurityMapper {

    //测试
    int insert(UserSecurity record);

    //通过userId查询全部
    UserSecurity selectAllByUserId(@Param("userId")Long userId);

    //返回邮箱给前端判定
    String selectEmailByUsername(@Param("username") String username);

    //返还手机给前端判定
    String selectPhoneByUsername(@Param("username") String username);

    //先查询邮箱是否已经被注册
    List<UserSecurity> selectAllByEmail(@Param("email") String email);

    //根据Token绑定的username更新email绑定邮箱
    int updateEmail(@Param("updatedEmail") String updatedEmail, @Param("username") String username);

    //根据chEmail查询username(Token)是否存在
    List<UserSecurity> selectAllByUsernameAndEmail(@Param("username") String username, @Param("chEmail") String chEmail);

    //根据Token的username和chPassword联表更新新密码
    int updatePassword(@Param("chPassword") String chPassword, @Param("username") String username);

    //根据username+phone联表更新密码
    int updatePasswordPhone(@Param("phonePassword") String phonePassword, @Param("username") String username);

    //根据username+card联表更新密码
    int updatePasswordCard(@Param("cardPassword") String cardPassword, @Param("username") String username);

    //先查询手机是否被注册
    List<UserSecurity> selectAllByPhone(@Param("phone") String phone);

    //绑定手机根据传过来的验证码更新手机号
    int updatePhoneByUsername(@Param("phone") String phone, @Param("username") String username);

    //先查询身份证是否被注册
    List<UserSecurity> selectAllByCardnumber(@Param("cardnumber") String cardnumber);

    //实名认证username+phone确认发送验证码
    List<UserSecurity> selectAllByUsernameAndPhone(@Param("phoneSFRZ") String phoneSFRZ, @Param("username") String username);

    //手机重置username+phone确认验证码
    List<UserSecurity> selectAllByPhoneAndUsername(@Param("phoneReset") String phoneReset, @Param("username") String username);

    //身份证重置密码
    List<UserSecurity> selectAllByRealnameAndCardnumber(@Param("cardName") String cardName, @Param("cardCode") String cardCode);

    //实名认证username作为条件更新自己的姓名和身份证
    int updateRealnameAndCardnumberByUsername(@Param("realnameSFRZ") String realnameSFRZ, @Param("cardNumber") String cardNumber, @Param("username") String username);

    //前端返回实名认证成功后的身份证号码
    String selectCardnumberByUsername(@Param("username") String username);

    //前端获得校园认证
    School selectSchool(@Param("username") String username);

    //确认传过来的username+realname是否存在
    String selectRealnameByUsername(@Param("username") String username);

    //校园认证查询schoolNumber
    String selectSchoolnumberByUsername(@Param("username") String username);

    //校园认证查询的School
    String selectSchoolByUsername(@Param("username") String username);

    //确认校园的信息的插入
    int updateSchoolByUsername(@Param("username") String username, @Param("schoolNumber") String schoolNumber, @Param("schoolName") String schoolName);

    //查询phone和username是否存在并且返回phone给逻辑判定
    String selectUsernameByPhone(@Param("ssPhone") String ssPhone);

    //查询邮箱是否已经被注册(多余)
    List<String> selectUsernameByEmail(@Param("ssEmail") String ssEmail);

    //如果邮箱没有被注册则更新(多余)
    int updateEmailByUsername(@Param("ssEmail") String ssEmail, @Param("username") String username);

    //查询手机号是否被注册(多余)
    List<Long> selectIdByPhone(@Param("phone") String phone);

    //更新新的手机号(多余)
    int updatePhone(@Param("newPhone") String newPhone, @Param("username") String username);

    //验证个人实名信息
    String selectUsernameByRealnameAndCardnumber(@Param("resetName") String resetName, @Param("resetCode") String resetCode);

    //返还整个实体表给前端
    UserSecurity selectAllByUsername(@Param("username") String username);

    //根据用户Id查询校园Id
    String selectSchoolnumberByUserId(@Param("userId")Long userId);

}