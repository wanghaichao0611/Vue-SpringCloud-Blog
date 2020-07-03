package com.pay.ServiceImpl;


import com.pay.Controller.AlipayCallbackController;
import com.pay.Entity.SelectBankcard;
import com.pay.Entity.SelectBankcardSign;
import com.pay.Entity.UserOrder;
import com.pay.Entity.UserWallet;
import com.pay.Feign.PayMemberFeign;
import com.pay.Feign.PayMsgFeign;
import com.pay.Mapper.UserOrderMapper;
import com.pay.Mapper.UserWalletMapper;
import com.pay.Request.PersonSvipRequest;
import com.pay.Request.PersonVipRequest;
import com.pay.Service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WalletServiceImpl implements WalletService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //curd的mapper
    @Autowired
    UserWalletMapper userWalletMapper;

    @Autowired
    UserOrderMapper userOrderMapper;

    @Autowired
    PayMemberFeign payMemberFeign;

    @Autowired
    PayMsgFeign payMsgFeign;

    //控制台
    private static Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

    //20个定长并发线程池,超过必须等待.
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    //获得整个支付表返还给前端
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserWallet getBankAll(Long id) {
        UserWallet userWallet=userWalletMapper.selectAllByWalletId(id);
        return userWallet;
    }

    //确认密码为null还是存在，不过要先确认是否完成实名注册
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int confirmWallet(Long id,String username) {
        //如果实名注册成功则判定是否已经签订了协议并且设置了密码
        String resultPassword=userWalletMapper.selectWalletPasswordByWalletId(id);
        //判定支付密码是否存在,如果存在则直接返回，不存在则需要先判定是否完成实名注册
        if (resultPassword==null) {
            //判定是否已经完成了实名注册(安全表中查询)
            String nameRegister = userWalletMapper.selectRealnameByUsername(username);
            if (nameRegister!=null) {
                //如果完成实名注册则可以把姓名更新到支付表中以备后来的银行卡备用
                //userWalletMapper.updateRealnameByWalletId(nameRegister, id);
                return SUCCESS;
            } else {
                return FAILED;
            }
        }
        return 2;
    }

    //更新支付密码和姓名(支付表，需要检验)
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int setWallet(Long id,String username, String walletPassword, String realName) {
        //判定支付表中是否存在,存在的话直接返回不用开通(支付表,设计表的时候不严谨，只能这样补救了)
        String nameRegisterPay=userWalletMapper.selectRealnameByWalletId(id);
        if (nameRegisterPay==null) {
            //安全表中查询姓名
            String nameRegister = userWalletMapper.selectRealnameByUsername(username);
            if (nameRegister.equals(realName)) {
                //如果两个姓名相等则写入姓名和支付密码
                userWalletMapper.updateWalletPasswordAndRealnameByWalletId(walletPassword,realName,id);
                return SUCCESS;
            }
        }
        return FAILED;
    }

    //获得余额返还给前端
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BigDecimal requireMoney(Long id) {
        BigDecimal money=userWalletMapper.selectWalletMoneyByWalletId(id);
        return money;
    }

    //查询银行卡返还给前端
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public SelectBankcard requireBank(Long id) {
        SelectBankcard bank=userWalletMapper.selectBankcard(id);
        return bank;
    }

    //银行卡的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int updateBank(String cardName, String myCard,Long id) {
        //先查询表中的标志是否为1即完成绑定
        SelectBankcardSign sign=userWalletMapper.selectBankcardSign(id);
        if (cardName.equals("中国建设银行") && sign.getBankcardOnesign()==0){
            userWalletMapper.updateBankcardOneAndBankcardnameOneByWalletId(myCard,cardName,id);
            return SUCCESS;
        }
        if (cardName.equals("中国工商银行") && sign.getBankcardTwosign()==0){
            userWalletMapper.updateBankcardTwoAndBankcardnameTwoByWalletId(myCard,cardName,id);
            return SUCCESS;
        }
        if (cardName.equals("中国银行") && sign.getBankcardThreesign()==0){
            userWalletMapper.updateBankcardThreeAndBankcardnameThreeByWalletId(myCard,cardName,id);
            return SUCCESS;
        }
        //一张类型的卡只能绑定一张
        return FAILED;
    }

    //查询三家银行卡的标志给前端
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public SelectBankcardSign requireSign(Long id) {
        SelectBankcardSign sign=userWalletMapper.selectBankcardSign(id);
        return sign;
    }

    //旧支付密码修改新支付密码
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int modifyPay(String oldPay, String newPay, Long id) {
        //首先先判定旧密码是否和库中一致
        String oldPassword=userWalletMapper.selectWalletPasswordByWalletId(id);
        if (oldPassword.equals(oldPay)){
            //如果两者相等则说明可以更新密码
            userWalletMapper.updateWalletPasswordByWalletId(newPay,id);
            return SUCCESS;
        }
        return FAILED;
    }
    //判定为哪种银行卡并且需要解绑
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int deleteBank(String cardName, String payPassword, Long id) {
        //首先先判定旧密码是否和库中一致
        String oldPassword=userWalletMapper.selectWalletPasswordByWalletId(id);
        if (oldPassword.equals(payPassword)){
            switch (cardName){
                case "中国建设银行":
                    userWalletMapper.updateOneSignByWalletId(id);
                    break;
                case "中国工商银行":
                    userWalletMapper.updateTwoSignByWalletId(id);
                    break;
                case "中国银行":
                    userWalletMapper.updateThreeSignByWalletId(id);
                    break;
            }
            return SUCCESS;
        }
        return FAILED;
    }

    //手机重置支付密码的更新
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void resetPay(String resetPassword, Long id) {
        //直接重置支付密码
        userWalletMapper.updateWalletPasswordByWalletId(resetPassword, id);
    }

    //查询最新的余额，只用来显示
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public BigDecimal selectNewMoney(Long id) {
        BigDecimal newMoney=userWalletMapper.selectWalletMoneyByWalletId(id);
        return newMoney;
    }

    //使用余额消费VIP
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int payVipBalance(PersonVipRequest personVipRequest, Long id) {
        //根据用户Id查询全部数据
        UserWallet userWallet=userWalletMapper.selectAllByWalletId(id);
        //如果密码不一致返返回0
        if (!userWallet.getWalletPassword().equals(personVipRequest.getWalletPassword())){
            return FAILED;
        }else if (userWallet.getWalletMoney().intValue()<personVipRequest.getBalanceVip().intValue()){
            //账号余额小于支付余额则直接回退返回2
            return 2;
        }else {
            //先更新支付账单
            UserOrder order=new UserOrder();
            order.setOrderId(id);
            order.setAppId("2016101500688517");
            order.setOutTradeNo(personVipRequest.getOut_trade_no());
            order.setSubject(personVipRequest.getSubject());
            order.setTotalAmount(personVipRequest.getBalanceVip());
            order.setBody(personVipRequest.getBody()+personVipRequest.getBalanceVipNumber()+"个月");
            order.setReceiptAmount(personVipRequest.getBalanceVip());
            order.setFundchannel("个人账户余额");
            order.setStoreId("2088102179614159");
            order.setTradestatus("支付成功");
            order.setCreatetime(new Date());
            order.setEndtime(new Date());
            userOrderMapper.insert(order);
            //最后直接更新数据库中的余额，也可以直接数据库结算
            BigDecimal end=new BigDecimal(
                    userWallet.getWalletMoney().intValue()-personVipRequest.getBalanceVip().intValue());
            //根据主键Id更新余额
            userWalletMapper.updateMoneyById(end,userWallet.getId());
            //后续会员操作直接多线程完成即可
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //交易成功，系统通知消费成功
                    payMsgFeign.sendSysMsg("用户余额消费","您已购买"+personVipRequest.getSubject()
                            +personVipRequest.getBalanceVip()+"月，一共花费余额"+personVipRequest.getBalanceVip().intValue()
                            +"元，祝您生活愉快!",id);
                    //再负载均衡完成指定时间的普通会员充值，余额消费不返还任何福利
                    try {
                        payMemberFeign.balanceVip(personVipRequest.getBalanceVipNumber(), id);
                    }catch (Exception e){
                        LOGGER.info("负载均衡会员出错误了");
                    }
                }
            });
            //返还成功标志1
            return SUCCESS;
        }
    }

    //使用余额消费SVIP
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int paySvipBalance(PersonSvipRequest personSvipRequest, Long id) {
        //根据用户Id查询全部数据
        UserWallet userWallet=userWalletMapper.selectAllByWalletId(id);
        //如果密码不一致返返回0
        if (!userWallet.getWalletPassword().equals(personSvipRequest.getWalletPassword())){
            return FAILED;
        }else if (userWallet.getWalletMoney().intValue()<personSvipRequest.getBalanceSvip().intValue()){
            //账号余额小于支付余额则直接回退返回2
            return 2;
        }else {
            //先更新支付账单
            UserOrder order=new UserOrder();
            order.setOrderId(id);
            order.setAppId("2016101500688517");
            order.setOutTradeNo(personSvipRequest.getOut_trade_no());
            order.setSubject(personSvipRequest.getSubject());
            order.setTotalAmount(personSvipRequest.getBalanceSvip());
            order.setBody(personSvipRequest.getBody()+personSvipRequest.getBalanceSvipNumber()+"个月");
            order.setReceiptAmount(personSvipRequest.getBalanceSvip());
            order.setFundchannel("个人账户余额");
            order.setStoreId("2088102179614159");
            order.setTradestatus("支付成功");
            order.setCreatetime(new Date());
            order.setEndtime(new Date());
            userOrderMapper.insert(order);
            //最后直接更新数据库中的余额，也可以直接数据库结算
            BigDecimal end=new BigDecimal(
                    userWallet.getWalletMoney().intValue()-personSvipRequest.getBalanceSvip().intValue());
            //根据主键Id更新余额
            userWalletMapper.updateMoneyById(end,userWallet.getId());
            //后续会员操作直接多线程完成即可
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //交易成功，系统通知消费成功
                    try{
                        payMsgFeign.sendSysMsg("用户余额消费","您已购买"+personSvipRequest.getSubject()
                                +personSvipRequest.getBalanceSvipNumber()+"月,一共花费余额"+personSvipRequest.getBalanceSvip().intValue()
                                +"元，祝您生活愉快!",id);
                    }catch (Exception e){
                        LOGGER.info("系统消息出错了!");
                    }
                    //再负载均衡完成指定时间的普通会员充值，余额消费不返还任何福利
                    try {
                        payMemberFeign.balanceSvip(personSvipRequest.getBalanceSvipNumber(), id);
                    }catch (Exception e){
                        LOGGER.info("负载均衡会员出错误了");
                    }
                }
            });
            //返还成功标志1
            return SUCCESS;
        }
    }
}
