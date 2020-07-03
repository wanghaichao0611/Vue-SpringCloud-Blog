package com.Controller;

import com.Service.UsernameToken;
import com.feign.UserPayFeign;
import com.mapper.UserMapperToken;
import com.requset.DateRequest;
import com.requset.PersonSvipRequest;
import com.requset.PersonVipRequest;
import com.tokenauthentication.annotation.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserPayController {

    //Token双向绑定取username
    @Autowired
    private HttpServletRequest requestUsername;

    //根据Headers中的TOKEN获得username
    @Autowired
    private UsernameToken usernameToken;

    //获得ID主键
    @Autowired
    private UserMapperToken userMapperToken;

    //pay的Feign
    @Autowired
    private UserPayFeign userPayFeign;

    //获取整个支付表返还给前端(安全)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getBankAllFeign")
    @AuthToken
    public Map<String, Object> getBankAllFeign() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.getBankAll(id);
    }

    //分页获取整个支付账单
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/orderPageAllFeign")
    @AuthToken
    public Map<String, Object> orderPageAllFeign(@RequestParam(name = "page", defaultValue = "1") int page,
                                                 @RequestParam(name = "count", defaultValue = "5") int count) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.orderPageAll(page, count, id);
    }

    //导出个人所有的账单(前端Csv)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/orderAllFeign")
    @AuthToken
    public Map<String, Object> orderAllFeign() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.orderAll(id);
    }

    //导出指定月份的账单
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/orderMonthAllFeign")
    @AuthToken
    public Map<String, Object> orderMonthAllFeign(DateRequest dateRequest) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.orderMonthAll(dateRequest,id);
    }

    //查询指定时间段的消费
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getCostFeign")
    @AuthToken
    public Map<String, Object> getCostFeign(DateRequest dateRequest) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.getCost(dateRequest,id);
    }

    //删除一个账单
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/orderDeleteFeign")
    @AuthToken
    public Map<String, Object> orderDeleteFeign(@RequestParam("orderId") Long orderId) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.orderDelete(orderId, id);
    }

    //我的钱包的确认(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/confirmWalletFeign")
    @AuthToken
    public Map<String, Object> confirmWalletFeign() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.confirmWallet(id, username);
    }

    //我的钱包的确认开通(支付密码+姓名)(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/setWalletFeign")
    @AuthToken
    public Map<String, Object> setWalletFeign(@RequestParam("walletPassword") String walletPassword, @RequestParam("realName") String realName) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.setWallet(id, username, walletPassword, realName);
    }

    //获得余额返还给前端(Token+以后备用)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getMoneyFeign")
    @AuthToken
    public Map<String, Object> getMoneyFeign() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.getMoney(id);
    }

    //获取银行卡给前端(Token+以后备用)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/getBankFeign")
    @AuthToken
    public Map<String, Object> getBankFeign() {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.getBank(id);
    }

    //更新银行卡(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/bindBankFeign")
    @AuthToken
    public Map<String, Object> bindBankFeign(@RequestParam("cardName") String cardName, @RequestParam("myCard") String myCard) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.bindBank(cardName, myCard, id);
    }

    //旧支付密码修改新支付密码(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/modifyPayFeign")
    @AuthToken
    public Map<String, Object> modifyPayFeign(@RequestParam("oldPay") String oldPay, @RequestParam("newPay") String newPay) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.modifyPay(oldPay, newPay, id);
    }

    //解绑中国建设银行卡(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/deleteBankOneFeign")
    @AuthToken
    public Map<String, Object> deleteBankOneFeign(@RequestParam("CCB") String CCB, @RequestParam("payPassword") String payPassword) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.deleteBankOne(CCB, payPassword, id);
    }

    //解绑中国工商银行卡(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/deleteBankTwoFeign")
    @AuthToken
    public Map<String, Object> deleteBankTwoFeign(@RequestParam("ICB") String ICB, @RequestParam("payPassword") String payPassword) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.deleteBankTwo(ICB, payPassword, id);
    }

    //解绑中国银行卡(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/deleteBankThreeFeign")
    @AuthToken
    public Map<String, Object> deleteBankThreeFeign(@RequestParam("CNB") String CNB, @RequestParam("payPassword") String payPassword) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.deleteBankThree(CNB, payPassword, id);
    }

    //手机号重置支付密码(Token)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/resetPayFeign")
    @AuthToken
    public Map<String, Object> resetPayFeign(@RequestParam("payCode") String payCode, @RequestParam("resetPassword") String resetPassword) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.resetPay(payCode, resetPassword, id, username);
    }

    //VIP原始支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/alipayVipFeign")
    @AuthToken
    public String alipayVipFeign(@RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
                                 @RequestParam("total_amount") String total_amount, @RequestParam("body") String body) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.alipayVip(out_trade_no, subject, total_amount, body, id);
    }

    //SVIP二维码支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/alipaySvipQrcodeFeign")
    @AuthToken
    public String alipaySvipQrcodeFeign(
            @RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
            @RequestParam("total_amount") String total_amount, @RequestParam("store_id") String store_id,
            @RequestParam("body") String body) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.alipaySvipQrcode(out_trade_no,subject,total_amount,store_id,body,id);
    }

    //查询最新的金额(只用来显示)
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectNewMoneyFeign")
    @AuthToken
    public Map<String, Object> selectNewMoneyFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.selectNewMoney(id);
    }


    //查询未使用过的优惠券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectCouponOffFeign")
    @AuthToken
    public Map<String, Object> selectCouponOffFeign(@RequestParam(name = "page", defaultValue = "1") int page,
                                                    @RequestParam(name = "count", defaultValue = "12") int count){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.selectCouponOff(page,count,id);
    }

    //兑换返还的金额券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/exchangeCouponFeign")
    @AuthToken
    public Map<String, Object> exchangeCouponFeign(@RequestParam("id")Long id,@RequestParam("orderId")Long orderId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long userId = userMapperToken.getId(username);
        return userPayFeign.exchangeCoupon(id,orderId,userId);
    }

    //查询使用过的优惠券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectCouponOnFeign")
    @AuthToken
    public Map<String, Object> selectCouponOnFeign(@RequestParam(name = "page", defaultValue = "1") int page,
                                                    @RequestParam(name = "count", defaultValue = "12") int count){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.selectCouponOn(page,count,id);
    }

    //删除已经使用过的兑换券
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/deleteCouponFeign")
    @AuthToken
    public Map<String, Object> deleteCouponFeign(@RequestParam("id")Long id,@RequestParam("orderId")Long orderId){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long userId = userMapperToken.getId(username);
        return userPayFeign.deleteCoupon(id,orderId,userId);
    }

    //用户充值，这个不给红包和任何优惠。
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/rechargeMoneyFeign")
    @AuthToken
    public String rechargeMoneyFeign(@RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
                                 @RequestParam("total_amount") String total_amount, @RequestParam("body") String body) {
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.rechargeMoney(out_trade_no, subject, total_amount, body, id);
    }

    //支付页面查询是否完成钱包注册，注册的话返回真实金额，不过只用来观看
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/selectBalanceFeign")
    @AuthToken
    public Map<String, Object> selectBalanceFeign(){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.selectBalance(id);
    }

    //使用个人余额充值VIP
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/payVipBalanceFeign")
    @AuthToken
    public Map<String, Object> payVipBalanceFeign(PersonVipRequest personVipRequest){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.payVipBalance(personVipRequest,id);
    }

    //使用个人余额充值VIP
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping("/paySvipBalanceFeign")
    @AuthToken
    public Map<String, Object> paySvipBalanceFeign(PersonSvipRequest personSvipRequest){
        String username = usernameToken.getUsername(requestUsername.getHeader("TOKEN"));
        Long id = userMapperToken.getId(username);
        return userPayFeign.paySvipBalance(personSvipRequest,id);
    }
}
