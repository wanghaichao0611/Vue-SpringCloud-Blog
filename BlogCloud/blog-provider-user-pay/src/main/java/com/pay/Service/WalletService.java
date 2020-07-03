package com.pay.Service;

import com.github.pagehelper.PageInfo;
import com.pay.Entity.SelectBankcard;
import com.pay.Entity.SelectBankcardSign;
import com.pay.Entity.UserWallet;
import com.pay.Request.PersonSvipRequest;
import com.pay.Request.PersonVipRequest;
import com.pay.Response.OrderResponse;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public interface WalletService {

    //获得整个支付表返还给前端
    UserWallet getBankAll(Long id);

    //查询是否开通了我的钱包
    int confirmWallet(Long id,String username);

    //更新支付密码和姓名
    int setWallet(Long id,String username,String walletPassword,String realName);

    //查询余额返还给前端
    BigDecimal requireMoney(Long id);

    //查询银行卡返还给前端
    SelectBankcard requireBank(Long id);

    //三家银行的更新服务
    int updateBank(String cardName,String myCard,Long id);

    //查询三家银行卡的标志给前端
    SelectBankcardSign requireSign(Long id);

    //通过旧支付密码修改新支付密码
    int modifyPay(String oldPay,String newPay,Long id);

    //判定为哪种银行卡并且需要解绑
    int deleteBank(String cardName,String payPassword,Long id);

    //手机重置支付密码的更新
    void resetPay(String resetPassword,Long id);

    //查询最新的余额，只用来显示
    BigDecimal selectNewMoney(Long id);

    //使用余额消费VIP
    int payVipBalance(PersonVipRequest personVipRequest,Long id);

    //使用余额消费SVIP
    int paySvipBalance(PersonSvipRequest personSvipRequest, Long id);
}
