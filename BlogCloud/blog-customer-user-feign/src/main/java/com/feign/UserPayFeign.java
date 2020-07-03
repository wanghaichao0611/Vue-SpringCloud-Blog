package com.feign;

import com.requset.DateRequest;
import com.requset.PersonSvipRequest;
import com.requset.PersonVipRequest;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "blog-provider-user-pay",fallbackFactory = UserPayFallback.class)
public interface UserPayFeign {


    //获得整个支付表返还给前端(安全)
    @RequestMapping(value = "/getBankAll", method = RequestMethod.POST)
    public Map<String,Object> getBankAll(@RequestParam("id") Long id);

    //分页获取整个支付账单
    @RequestMapping(value = "/orderPageAll", method = RequestMethod.POST)
    public Map<String,Object> orderPageAll(@RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "count", defaultValue = "5") int count,
                                                @RequestParam("id")Long id);

    //获得个人所有的账单
    @RequestMapping(value = "/orderAll", method = RequestMethod.POST)
    public Map<String, Object> orderAll(@RequestParam("id")Long id);

    //获得指定月份的账单
    @RequestMapping(value = "/orderMonthAll", method = RequestMethod.POST)
    public Map<String, Object> orderMonthAll(@RequestBody DateRequest request, @RequestParam("id") Long id);

    //查询指定时间段的消费
    @RequestMapping(value = "/getCost", method = RequestMethod.POST)
    public Map<String, Object> getCost(@RequestBody DateRequest request, @RequestParam("id") Long id);

    //删除一条账单的记录
    @RequestMapping(value = "/orderDelete", method = RequestMethod.POST)
    public Map<String,Object> orderDelete(@RequestParam("orderId")Long orderId,@RequestParam("authorId")Long authorId);

    //确认是否可以开通钱包
    @RequestMapping(value = "/confirmWallet", method = RequestMethod.POST)
    public Map<String, Object> confirmWallet(@RequestParam("id") Long id,@RequestParam("username") String username);

    //钱包的确认密码和姓名
    @RequestMapping(value = "/setWallet", method = RequestMethod.POST)
    public Map<String,Object> setWallet(@RequestParam("id") Long id,@RequestParam("username")String username,
                                        @RequestParam("walletPassword") String walletPassword,
                                        @RequestParam("realName") String realName);

    //获得余额返还给前端
    @RequestMapping(value = "/getMoney", method = RequestMethod.POST)
    public Map<String,Object> getMoney(@RequestParam("id") Long id);

    //获取银行卡给前端
    @RequestMapping(value = "/getBank", method = RequestMethod.POST)
    public Map<String,Object> getBank(@RequestParam("id") Long id);

    //更新银行卡
    @RequestMapping(value = "/bindBank", method = RequestMethod.POST)
    public Map<String,Object> bindBank(@RequestParam("cardName") String cardName,@RequestParam("myCard") String myCard,
                                       @RequestParam("id") Long id);

    //旧密码修改支付密码
    @RequestMapping(value = "/modifyPay", method = RequestMethod.POST)
    public Map<String,Object> modifyPay(@RequestParam("oldPay") String oldPay,@RequestParam("newPay") String newPay,
                                        @RequestParam("id") Long id);

    //解绑中国建设银行卡
    @RequestMapping(value = "/deleteBankOne", method = RequestMethod.POST)
    public Map<String,Object> deleteBankOne(@RequestParam("CCB") String CCB,@RequestParam("payPassword") String payPassword,
                                            @RequestParam("id") Long id);

    //解绑中国工商银行卡
    @RequestMapping(value = "/deleteBankTwo", method = RequestMethod.POST)
    public Map<String,Object> deleteBankTwo(@RequestParam("ICB") String ICB,@RequestParam("payPassword") String payPassword,
                                            @RequestParam("id") Long id);


    //解绑中国银行卡
    @RequestMapping(value = "/deleteBankThree", method = RequestMethod.POST)
    public Map<String,Object> deleteBankThree(@RequestParam("CNB") String CNB,@RequestParam("payPassword") String payPassword,
                                            @RequestParam("id") Long id);

    //手机号重置支付密码
    @RequestMapping(value = "/resetPay", method = RequestMethod.POST)
    public Map<String,Object> resetPay(@RequestParam("payCode") String payCode,@RequestParam("resetPassword") String resetPassword,
                                       @RequestParam("id") Long id,@RequestParam("username") String username);

    //VIP原始支付
    @RequestMapping(value = "/alipayVip", method = RequestMethod.POST)
    public String alipayVip(@RequestParam("out_trade_no") String out_trade_no,@RequestParam("subject") String subject,
                         @RequestParam("total_amount")String total_amount,@RequestParam("body") String body,@RequestParam("id") Long id);

    //SVIP二维码支付
    @RequestMapping(value = "/alipaySvipQrcode", method = RequestMethod.POST)
    public String alipaySvipQrcode(
            @RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
            @RequestParam("total_amount") String total_amount, @RequestParam("store_id") String store_id,
            @RequestParam("body") String body,@RequestParam("id") Long id);

    //查询最新的金额(只用来显示)
    @RequestMapping(value = "/selectNewMoney", method = RequestMethod.POST)
    public Map<String, Object> selectNewMoney(@RequestParam("id") Long id);

    //查询未使用过的优惠券
    @RequestMapping(value = "/selectCouponOff", method = RequestMethod.POST)
    public Map<String, Object> selectCouponOff(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "count", defaultValue = "12") int count,
            @RequestParam("id") Long id);

    //兑换返还的金额券
    @RequestMapping(value = "/exchangeCoupon", method = RequestMethod.POST)
    public Map<String, Object> exchangeCoupon(@RequestParam("id")Long id,@RequestParam("orderId")Long orderId,
                                              @RequestParam("userId")Long userId);

    //查询使用过的优惠券
    @RequestMapping(value = "/selectCouponOn", method = RequestMethod.POST)
    public Map<String, Object> selectCouponOn(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "count", defaultValue = "12") int count,
            @RequestParam("id") Long id);

    //删除兑换过的金额券
    @RequestMapping(value = "/deleteCoupon", method = RequestMethod.POST)
    public Map<String, Object> deleteCoupon(@RequestParam("id")Long id,@RequestParam("orderId")Long orderId,
                                              @RequestParam("userId")Long userId);

    //用户个人充值的原始支付
    @RequestMapping(value = "/rechargeMoney", method = RequestMethod.POST)
    public String rechargeMoney(@RequestParam("out_trade_no") String out_trade_no,@RequestParam("subject") String subject,
                            @RequestParam("total_amount")String total_amount,@RequestParam("body") String body,@RequestParam("id") Long id);

    //支付页面查询余额
    @RequestMapping(value = "/selectBalance", method = RequestMethod.POST)
    public Map<String, Object> selectBalance(@RequestParam("id")Long id);

    //个人余额充值VIP
    @RequestMapping(value = "/payVipBalance", method = RequestMethod.POST)
    public Map<String, Object> payVipBalance(@RequestBody PersonVipRequest personVipRequest,@RequestParam("id")Long id);

    //个人余额充值SVIP
    @RequestMapping(value = "/paySvipBalance", method = RequestMethod.POST)
    public Map<String, Object> paySvipBalance(@RequestBody PersonSvipRequest personSvipRequest, @RequestParam("id")Long id);

}
@Component
class UserPayFallback implements FallbackFactory<UserPayFeign> {

    //日志输出到控制台
    private static final Logger LOGGER= LoggerFactory.getLogger(UserPayFallback.class);

    @Override
    public UserPayFeign create(Throwable throwable) {
        return new UserPayFeign() {

            //获取整个支付表的fallback
            @Override
            public Map<String, Object> getBankAll(Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //获取整个支付账单的fallback
            @Override
            public Map<String, Object> orderPageAll(int page, int count, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取账单超时!");
                return feignMap;
            }

            //获得个人所有的账单的fallback
            @Override
            public Map<String, Object> orderAll(Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取个人账单超时!");
                return feignMap;
            }

            //获得指定月份的账单
            @Override
            public Map<String, Object> orderMonthAll(DateRequest request, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取指定账单超时!");
                return feignMap;
            }

            //查询指定时间段的消费
            @Override
            public Map<String, Object> getCost(DateRequest request, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取消费超时!");
                return feignMap;
            }

            //删除一条账单的记录的fallback
            @Override
            public Map<String, Object> orderDelete(Long orderId, Long authorId) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "删除账单超时!");
                return feignMap;
            }

            //开通钱包的fallback
            @Override
            public Map<String, Object> confirmWallet(Long id,String username) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取超时!");
                return feignMap;
            }

            //钱包的确认开通的fallback
            @Override
            public Map<String, Object> setWallet(Long id, String username, String walletPassword, String realName) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "开通超时!");
                return feignMap;
            }

            //获得余额返还给前端的fallback
            @Override
            public Map<String, Object> getMoney(Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取失败!");
                return feignMap;
            }

            //获得银行卡返还给前端的fallback
            @Override
            public Map<String, Object> getBank(Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "获取失败!");
                return feignMap;
            }

            //更新银行卡的服务的fallback
            @Override
            public Map<String, Object> bindBank(String cardName, String myCard, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "绑定失败!");
                return feignMap;
            }

            //旧支付密码修改新支付密码的fallback
            @Override
            public Map<String, Object> modifyPay(String oldPay, String newPay, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "修改超时!");
                return feignMap;
            }

            //解绑中国建设银行卡的fallback
            @Override
            public Map<String, Object> deleteBankOne(String CCB, String payPassword, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "解绑超时!");
                return feignMap;
            }

            //解绑中国工商银行卡的fallback
            @Override
            public Map<String, Object> deleteBankTwo(String ICB, String payPassword, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "解绑超时!");
                return feignMap;
            }

            //解绑中国银行卡的fallback
            @Override
            public Map<String, Object> deleteBankThree(String CNB, String payPassword, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "解绑超时!");
                return feignMap;
            }

            //手机号重置支付密码的fallback
            @Override
            public Map<String, Object> resetPay(String payCode, String resetPassword, Long id, String username) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "重置超时!");
                return feignMap;
            }

            //VIP原始支付的fallback
            @Override
            public String alipayVip(String out_trade_no, String subject, String total_amount, String body, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                return "VIP支付超时!";
            }

            //SVIP二维码支付的fallback
            @Override
            public String alipaySvipQrcode(String out_trade_no, String subject, String total_amount, String store_id,String body, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                return "SVIP支付超时!";
            }

            //查询最新的金额(只用来显示)fallback
            @Override
            public Map<String, Object> selectNewMoney(Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询余额超时!");
                return feignMap;
            }

            //查询未使用过的优惠券的fallback
            @Override
            public Map<String, Object> selectCouponOff(int page,int count,Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询未使用过的优惠券超时!");
                return feignMap;
            }

            //兑换返还的金额券的fallback
            @Override
            public Map<String, Object> exchangeCoupon(Long id,Long orderId, Long userId) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "返还优惠券超时!");
                return feignMap;
            }

            //查询使用过的优惠券的fallback
            @Override
            public Map<String, Object> selectCouponOn(int page, int count, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "查询使用过的优惠券超时!");
                return feignMap;
            }

            //删除兑换过的金额券的fallback
            @Override
            public Map<String, Object> deleteCoupon(Long id, Long orderId, Long userId) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "删除返还优惠券超时!");
                return feignMap;
            }

            //用户个人充值的原始支付的fallback
            @Override
            public String rechargeMoney(String out_trade_no, String subject, String total_amount, String body, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                return "用户个人充值支付超时!";
            }

            //支付页面查询余额的fallback
            @Override
            public Map<String, Object> selectBalance(Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "支付查询页面超时!");
                return feignMap;
            }

            //个人余额充值VIP的fallback
            @Override
            public Map<String, Object> payVipBalance(PersonVipRequest personVipRequest, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "余额充值VIP超时!");
                return feignMap;
            }

            //个人余额充值SVIP的fallback
            @Override
            public Map<String, Object> paySvipBalance(PersonSvipRequest personSvipRequest, Long id) {
                UserPayFallback.LOGGER.info("造成回退的原因是:",throwable);
                Map<String, Object> feignMap = new HashMap<>();
                feignMap.put("message", "余额充值SVIP超时!");
                return feignMap;
            }
        };
    }
}
