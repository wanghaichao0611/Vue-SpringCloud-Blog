package com.pay.Mapper;
import java.util.List;

import com.pay.Entity.SelectBankcard;
import com.pay.Entity.SelectBankcardSign;
import com.pay.Entity.SelectBankcardname;import com.pay.Entity.UserWallet;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Mapper
public interface UserWalletMapper {

    //测试
    int insert(UserWallet record);

    //查询这个表并且上安全验证
    UserWallet selectAllByWalletId(@Param("walletId")Long walletId);

    //判定原来的表中的是否完成注册
    String selectRealnameByUsername(@Param("username") String username);

    //判定是否已经完成注册(未使用)
    String selectRealnameByWalletId(@Param("walletId") Long walletId);

    //查询开通密码
    String selectWalletPasswordByWalletId(@Param("walletId") Long walletId);

    //根据id插入姓名到支付表中
    void updateRealnameByWalletId(@Param("updatedRealname") String updatedRealname, @Param("walletId") Long walletId);

    //更新支付密码和姓名
    int updateWalletPasswordAndRealnameByWalletId(@Param("walletPassword") String walletPassword, @Param("updatedRealname") String updatedRealname, @Param("walletId") Long walletId);

    //获得余额返还给前端
    BigDecimal selectWalletMoneyByWalletId(@Param("walletId") Long walletId);

    //查询银行卡给前端
    SelectBankcard selectBankcard(@Param("walletId") Long walletId);

    //更新中国建设银行One
    int updateBankcardOneAndBankcardnameOneByWalletId(@Param("updatedBankcardOne") String updatedBankcardOne, @Param("updatedBankcardnameOne") String updatedBankcardnameOne, @Param("walletId") Long walletId);

    //更新中国工商银行Two
    int updateBankcardTwoAndBankcardnameTwoByWalletId(@Param("updatedBankcardTwo") String updatedBankcardTwo, @Param("updatedBankcardnameTwo") String updatedBankcardnameTwo, @Param("walletId") Long walletId);

    //更新中国银行Three
    int updateBankcardThreeAndBankcardnameThreeByWalletId(@Param("updatedBankcardThree") String updatedBankcardThree, @Param("updatedBankcardnameThree") String updatedBankcardnameThree, @Param("walletId") Long walletId);

    //查询绑定过银行卡的名字
    SelectBankcardname selectBankcardname(@Param("walletId") Long walletId);

    //查询表中的三个标志
    SelectBankcardSign selectBankcardSign(@Param("walletId")Long walletId);

    //通过旧密码修改新支付密码通过外键ID
    int updateWalletPasswordByWalletId(@Param("updatedWalletPassword")String updatedWalletPassword,@Param("walletId")Long walletId);

    //删除指定中国建设银行银行卡并且修改绑定标志
    int updateOneSignByWalletId(@Param("walletId")Long walletId);

    //删除指定中国工商银行银行卡并且修改绑定标志
    int updateTwoSignByWalletId(@Param("walletId")Long walletId);

    //删除指定中国银行银行卡并且修改绑定标志
    int updateThreeSignByWalletId(@Param("walletId")Long walletId);

    //根据用户Id查询钱包的主键Id
    Long selectIdByWalletId(@Param("walletId")Long walletId);

    //根据主键Id更新余额（优惠券增加余额）
    int updateWalletMoneyById(@Param("updatedWalletMoney")BigDecimal updatedWalletMoney,@Param("id")Long id);

    //根据主键Id更新余额（消费更新余额）
    int updateMoneyById(@Param("updatedWalletMoney")BigDecimal updatedWalletMoney,@Param("id")Long id);

}