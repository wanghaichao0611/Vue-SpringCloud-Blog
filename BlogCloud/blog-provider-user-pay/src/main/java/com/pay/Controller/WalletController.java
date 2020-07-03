package com.pay.Controller;

import com.github.pagehelper.PageInfo;
import com.pay.Entity.SelectBankcard;
import com.pay.Entity.SelectBankcardSign;
import com.pay.Entity.UserWallet;
import com.pay.Feign.UserPaySecurityFeign;
import com.pay.Mapper.UserWalletMapper;
import com.pay.Request.PersonSvipRequest;
import com.pay.Request.PersonVipRequest;
import com.pay.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WalletController {

    //我的钱包的服务
    @Autowired
    private WalletService walletService;

    //支付安全验证的服务消费者(Feign)
    @Autowired
    private UserPaySecurityFeign userPaySecurityFeign;

    //查询整个支付表并且上安全验证给前端
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/getBankAll")
    public Map<String,Object> getBankAll(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //查询整个表并且隐藏并且上安全
        UserWallet userWallet=walletService.getBankAll(id);
        String bankcardOne=userWallet.getBankcardOne();
        String bankcardTwo=userWallet.getBankcardTwo();
        String bankcardThree=userWallet.getBankcardThree();
        String bankcardName=userWallet.getRealname();
        if (bankcardOne!=null){
            bankcardOne=bankcardOne.replaceAll("(\\d{0})\\d{15}(\\d{4})", "$1****$2");
            userWallet.setBankcardOne(bankcardOne);
        }
        if (bankcardTwo!=null){
            bankcardTwo=bankcardTwo.replaceAll("(\\d{0})\\d{15}(\\d{4})", "$1****$2");
            userWallet.setBankcardTwo(bankcardTwo);
        }
        if (bankcardThree!=null){
            bankcardThree=bankcardThree.replaceAll("(\\d{0})\\d{15}(\\d{4})", "$1****$2");
            userWallet.setBankcardThree(bankcardThree);
        }
        userWallet.setWalletPassword("你愁啥？？？");
        if (bankcardName!=null){
            bankcardName=bankcardName.replaceAll("([\\d\\D]{1})(.*)", "$1**");
            userWallet.setRealname(bankcardName);
        }
        resultMap.put("success", true);
        resultMap.put("bank",userWallet);
        return resultMap;
    }

    /*钱包的确认*/
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/confirmWallet")
    public Map<String,Object> confirmWallet(@RequestParam("id") Long id,@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //首先先查询钱包密码是否为null值并且需要先完成实名认证
        int result = walletService.confirmWallet(id,username);
        //直接传递给前端信号让前端自己判定
        resultMap.put("success", result);
        return resultMap;
    }

    //钱包的开通
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/setWallet")
    public Map<String,Object> setWallet(@RequestParam("id") Long id,@RequestParam("username")String username,
                                        @RequestParam("walletPassword") String walletPassword,
                                        @RequestParam("realName") String realName){
        Map<String, Object> resultMap = new HashMap<>();
        //首先先确定姓名是否一致，并且需要把一致的姓名保存到支付表中
        int result=walletService.setWallet(id, username, walletPassword, realName);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "密码设置成功":"姓名不正确");
        return resultMap;
    }

    //余额返还给前端(暂时没用)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/getMoney")
    public Map<String,Object> getMoney(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //查询余额返还给前端
        BigDecimal money=walletService.requireMoney(id);
        resultMap.put("success", true);
        resultMap.put("money",money);
        return resultMap;
    }

    //查询银行卡给前端显示(暂时没用)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/getBank")
    public Map<String,Object> getBank(@RequestParam("id") Long id) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询所绑定银行卡给前端
        SelectBankcard bankcards = walletService.requireBank(id);
        //查询银行卡的标志给前端
        SelectBankcardSign bankcardSign=walletService.requireSign(id);
        resultMap.put("bank", bankcards);
        resultMap.put("sign", bankcardSign);
        return resultMap;
    }

    //绑定银行卡(微服务调用手机验证码)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/bindBank")
    public Map<String,Object> bindBank(@RequestParam("cardName") String cardName,@RequestParam("myCard") String myCard,
                                       @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //更新银行卡
        int result=walletService.updateBank(cardName, myCard, id);
        if (result>0) {
            //查询所绑定银行卡给前端
            SelectBankcard bankcards = walletService.requireBank(id);
            String bankcardOne=bankcards.getBankcardOne();
            String bankcardTwo=bankcards.getBankcardTwo();
            String bankcardThree=bankcards.getBankcardThree();
            if (bankcardOne!=null){
                bankcardOne=bankcardOne.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                bankcards.setBankcardOne(bankcardOne);
            }
            if (bankcardTwo!=null){
                bankcardTwo=bankcardTwo.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                bankcards.setBankcardTwo(bankcardTwo);
            }
            if (bankcardThree!=null){
                bankcardThree=bankcardThree.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                bankcards.setBankcardThree(bankcardThree);
            }
            //查询银行卡的标志给前端
            SelectBankcardSign bankcardSign = walletService.requireSign(id);
            resultMap.put("sign", bankcardSign);
            resultMap.put("bank", bankcards);
        }
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "绑定成功":"一种类型的银行只能绑定一张卡");
        return resultMap;

    }

    //修改支付密码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/modifyPay")
    public Map<String,Object> modifyPay(@RequestParam("oldPay") String oldPay,@RequestParam("newPay") String newPay,
                                       @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //旧支付密码修改新支付密码
        int result=walletService.modifyPay(oldPay, newPay, id);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "修改成功!":"旧支付密码存在错误");
        return resultMap;
    }

    //解绑中国建设银行卡
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/deleteBankOne")
    public Map<String,Object> deleteBankOne(@RequestParam("CCB") String CCB,@RequestParam("payPassword") String payPassword,
                                         @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //判定为哪种银行卡
        int result=walletService.deleteBank(CCB,payPassword,id);
        if (result>0) {
            //查询整个表并且隐藏并且上安全,只返还安全的卡号和标志
            UserWallet userWallet=walletService.getBankAll(id);
            String bankcardOne=userWallet.getBankcardOne();
            String bankcardTwo=userWallet.getBankcardTwo();
            String bankcardThree=userWallet.getBankcardThree();
            if (bankcardOne!=null){
                bankcardOne=bankcardOne.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardOne(bankcardOne);
            }
            if (bankcardTwo!=null){
                bankcardTwo=bankcardTwo.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardTwo(bankcardTwo);
            }
            if (bankcardThree!=null){
                bankcardThree=bankcardThree.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardThree(bankcardThree);
            }
            //查询银行卡的标志给前端
            SelectBankcardSign bankcardSign = walletService.requireSign(id);
            resultMap.put("sign", bankcardSign);
            resultMap.put("bankcardOne", userWallet.getBankcardOne());
            resultMap.put("bankcardTwo", userWallet.getBankcardTwo());
            resultMap.put("bankcardThree", userWallet.getBankcardThree());
        }
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "解绑成功!":"旧支付密码存在错误");
        return resultMap;
    }

    //解绑中国工商银行卡
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/deleteBankTwo")
    public Map<String,Object> deleteBankTwo(@RequestParam("ICB") String ICB,@RequestParam("payPassword") String payPassword,
                                         @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //判定为哪种银行卡
        int result=walletService.deleteBank(ICB,payPassword,id);
        if (result>0) {
            //查询整个表并且隐藏并且上安全,只返还安全的卡号和标志
            UserWallet userWallet=walletService.getBankAll(id);
            String bankcardOne=userWallet.getBankcardOne();
            String bankcardTwo=userWallet.getBankcardTwo();
            String bankcardThree=userWallet.getBankcardThree();
            if (bankcardOne!=null){
                bankcardOne=bankcardOne.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardOne(bankcardOne);
            }
            if (bankcardTwo!=null){
                bankcardTwo=bankcardTwo.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardTwo(bankcardTwo);
            }
            if (bankcardThree!=null){
                bankcardThree=bankcardThree.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardThree(bankcardThree);
            }
            //查询银行卡的标志给前端
            SelectBankcardSign bankcardSign = walletService.requireSign(id);
            resultMap.put("sign", bankcardSign);
            resultMap.put("bankcardOne", userWallet.getBankcardOne());
            resultMap.put("bankcardTwo", userWallet.getBankcardTwo());
            resultMap.put("bankcardThree", userWallet.getBankcardThree());
        }
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "解绑成功!":"旧支付密码存在错误");
        return resultMap;
    }

    //解绑中国银行卡
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/deleteBankThree")
    public Map<String,Object> deleteBankThree(@RequestParam("CNB") String CNB,@RequestParam("payPassword") String payPassword,
                                            @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //判定为哪种银行卡
        int result=walletService.deleteBank(CNB,payPassword,id);
        if (result>0) {
            //查询整个表并且隐藏并且上安全
            UserWallet userWallet=walletService.getBankAll(id);
            String bankcardOne=userWallet.getBankcardOne();
            String bankcardTwo=userWallet.getBankcardTwo();
            String bankcardThree=userWallet.getBankcardThree();
            if (bankcardOne!=null){
                bankcardOne=bankcardOne.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardOne(bankcardOne);
            }
            if (bankcardTwo!=null){
                bankcardTwo=bankcardTwo.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardTwo(bankcardTwo);
            }
            if (bankcardThree!=null){
                bankcardThree=bankcardThree.replaceAll("(\\d{1})\\d{17}(\\d{1})", "$1****$2");
                userWallet.setBankcardThree(bankcardThree);
            }
            //查询银行卡的标志给前端
            SelectBankcardSign bankcardSign = walletService.requireSign(id);
            resultMap.put("sign", bankcardSign);
            resultMap.put("bankcardOne", userWallet.getBankcardOne());
            resultMap.put("bankcardTwo", userWallet.getBankcardTwo());
            resultMap.put("bankcardThree", userWallet.getBankcardThree());
        }
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "解绑成功!":"旧支付密码存在错误");
        return resultMap;
    }

    //手机验证码重置支付密码
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/resetPay")
    public Map<String,Object> resetPay(@RequestParam("payCode") String payCode,@RequestParam("resetPassword") String resetPassword,
                                              @RequestParam("id") Long id,@RequestParam("username") String username){
        Map<String, Object> resultMap = new HashMap<>();
        //负载均衡测试验证码的feign
        Map<String, Object>  resultMapSecurity=userPaySecurityFeign.checkServerCode(payCode, username);
        Object code=resultMapSecurity.get("result");
        if (code.equals("success")){
            //如果feign负载均衡验证成功则更新支付密码
            walletService.resetPay(resetPassword, id);
            resultMap.put("success",true);
            resultMap.put("message","重置密码成功!");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("message","验证码错误或者失效!");
            return resultMap;
        }
    }

    //查询最新的余额，只用来显示
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectNewMoney")
    public Map<String, Object> selectNewMoney(@RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        BigDecimal newMoney=walletService.selectNewMoney(id);
        resultMap.put("success",true);
        resultMap.put("myMoney",newMoney);
        return resultMap;
    }

    //支付页面查询余额，没开通则无法使用余额功能
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/selectBalance")
    public Map<String, Object> selectBalance(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //查询整个钱包表
        UserWallet userWallet=walletService.getBankAll(id);
        if (userWallet.getWalletPassword()==null || userWallet.getWalletPassword().equals("")){
            //没有完成直接返回失败，提醒要去完成钱包注册
            resultMap.put("success",false);
            resultMap.put("myMoney","您未完成钱包绑定，请及时绑定");
        }else {
            resultMap.put("success",true);
            resultMap.put("myMoney",userWallet.getWalletMoney());
        }
        return resultMap;
    }

    //个人余额充值VIP
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/payVipBalance")
    public Map<String, Object> payVipBalance(@RequestBody PersonVipRequest personVipRequest, @RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //核实数据库中的数据
        int resultVip=walletService.payVipBalance(personVipRequest,id);
        if (resultVip==0){
            resultMap.put("success",0);
            resultMap.put("message","您的支付密码存在错误!");
        }else if (resultVip==2){
            resultMap.put("success",2);
            resultMap.put("message","您的账户余额不足");
        }else {
            resultMap.put("success",1);
        }
        return resultMap;
    }

    //个人余额充值SVIP
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/paySvipBalance")
    public Map<String, Object> paySvipBalance(@RequestBody PersonSvipRequest personSvipRequest, @RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        //核实数据库中的数据
        int resultSvip=walletService.paySvipBalance(personSvipRequest,id);
        if (resultSvip==0){
            resultMap.put("success",0);
            resultMap.put("message","您的支付密码存在错误!");
        }else if (resultSvip==2){
            resultMap.put("success",2);
            resultMap.put("message","您的账户余额不足");
        }else {
            resultMap.put("success",1);
        }
        return resultMap;
    }

}
