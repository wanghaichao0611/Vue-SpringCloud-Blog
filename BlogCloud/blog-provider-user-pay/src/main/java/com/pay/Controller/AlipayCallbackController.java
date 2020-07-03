package com.pay.Controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.pay.Bean.AlipayConfig;
import com.pay.Bean.AlipayNotifyParam;
import com.pay.Config.WebSocket;
import com.pay.Entity.UserOrder;
import com.pay.Entity.UserWallet;
import com.pay.Feign.PayMemberFeign;
import com.pay.Mapper.UserOrderMapper;
import com.pay.Mapper.UserWalletMapper;
import com.pay.Util.PayChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//支付宝回调接口(必须以异步通知的结果为主且需要验证签名,防止黑客篡改)
@RestController
public class AlipayCallbackController {

    //调用会员微服务的Feign
    @Autowired
    PayMemberFeign payMemberFeign;

    //Crud的Mapper
    @Autowired
    UserOrderMapper userOrderMapper;

    //Crud的Mapper
    @Autowired
    UserWalletMapper userWalletMapper;

    //websocket提示跳转页面
    @Autowired
    WebSocket webSocket;

    //控制台
    private static Logger logger = LoggerFactory.getLogger(AlipayCallbackController.class);

    //20个定长并发线程池,超过必须等待.
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    /**
     * //支付宝文档中的解释
     * 第一步:验证签名,签名通过后进行第二步
     * 第二步:按一下步骤进行验证
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     * 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
     */


    //应该都以主键Id更新，不应该使用非主键Id更新
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/alipayCallback")
    public String callback(HttpServletRequest request) {
        Map<String, String> params = convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        logger.info("支付宝回调，{}", paramsJson);
        try {
            AlipayConfig alipayConfig = new AlipayConfig();// 支付宝配置
            // 调用SDK验证签名,防止黑客篡改,文档要求保证一致性。
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(),
                    alipayConfig.getCharset(), alipayConfig.getSigntype());
            if (signVerified) {
                logger.info("支付宝回调签名认证成功");
                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                this.check(params);
                // 另起线程处理业务
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        AlipayNotifyParam param = buildAlipayNotifyParam(params);
                        String trade_status = param.getTradeStatus();
                        // 支付成功
                        if (trade_status.equals("TRADE_SUCCESS")
                                || trade_status.equals("TRADE_FINISHED")) {
                            // 处理支付成功逻辑
                            try {
                                Date date=new Date();
                                //打开Jedis通过商户订单号找到ID给会员更新服务
                                Jedis jedis = new Jedis("localhost", 6379);
                                Long id=Long.valueOf(jedis.get(params.get("out_trade_no")));
                                jedis.del(params.get("out_trade_no"));
                                //根据宇宙唯一订单号查询整个支付表(第二种延迟队列的方式，比Redis好用些)
                                UserOrder userOrder=userOrderMapper.selectAllByOutTradeNo(params.get("out_trade_no"));
                               if (param.getTotalAmount().toString().equals("150.00") && param.getSubject().equals("VIP年费")){
                                   //调用会员微服务直接更新VIP年费的天数且完成订单表的更新
                                   Map<String, Object>  resultMapMemberYear=payMemberFeign.vipYear(id);
                                   Object code=resultMapMemberYear.get("result");
                                   if (code.equals("success")){
                                       //成功的话则更新订单表的状态
                                       userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                               "支付成功",
                                               PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"")+15,
                                                       param.getFundBillList().indexOf("\"}]"))),
                                               date,param.getOutTradeNo(),id);
                                   }else {
                                       //写入失败的日志(补偿),暂时不会,基本成功.
                                   }
                               }
                               if (param.getTotalAmount().toString().equals("50.00") && param.getSubject().equals("VIP包季")){
                                   //调用会员微服务直接更新VIP包季的天数且完成订单表的更新
                                   Map<String, Object>  resultMapMemberThree=payMemberFeign.vipThreeMonths(id);
                                   Object code=resultMapMemberThree.get("result");
                                   if (code.equals("success")){
                                       //成功更新订单表的状态
                                       userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                               "支付成功",
                                               PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"")+15,
                                                       param.getFundBillList().indexOf("\"}]"))),
                                               date,param.getOutTradeNo(),id);
                                   }else {
                                       //写入失败的日志(补偿),暂时不会,基本成功.
                                   }
                               }
                               if (param.getTotalAmount().toString().equals("20.00") && param.getSubject().equals("VIP包月")){
                                   //调用会员微服务直接更新VIP包月的天数且完成订单表的更新
                                   Map<String, Object>  resultMapMemberMonth=payMemberFeign.vipMonth(id);
                                   Object code=resultMapMemberMonth.get("result");
                                   if (code.equals("success")){
                                       //成功的话则更新订单表的状态
                                       userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                               "支付成功",
                                               PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"")+15,
                                                       param.getFundBillList().indexOf("\"}]"))),
                                               date,param.getOutTradeNo(),id);
                                   }else {
                                       //写入失败的日志(补偿),暂时不会,基本成功.
                                   }
                               }
                               if (param.getTotalAmount().toString().equals("300.00") && param.getSubject().equals("SVIP年费")) {
                                    //调用会员微服务直接更新SVIP年费的天数且完成订单表的更新.
                                    Map<String, Object>  resultMapMemberSvipYear=payMemberFeign.svipYear(id);
                                    Object code = resultMapMemberSvipYear.get("result");
                                    if (code.equals("success")) {
                                        //成功的话则更新订单表的状态
                                        userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                                "支付成功",
                                                PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"") + 15,
                                                        param.getFundBillList().indexOf("\"}]"))),
                                                date, param.getOutTradeNo(), id);
                                        //webSocket提醒给用户跳转页面
                                        webSocket.sendTextMessage(id.toString(),"member/power");
                                    } else {
                                        //写入失败的日志(补偿),暂时不会,基本成功.
                                    }
                                }
                               if (param.getTotalAmount().toString().equals("80.00") && param.getSubject().equals("SVIP包季")) {
                                    //调用会员微服务直接更新SVIP包季的天数且完成订单表的更新.
                                    Map<String, Object>  resultMapMemberSvipThreeMonths=payMemberFeign.svipThreeMonths(id);
                                    Object code = resultMapMemberSvipThreeMonths.get("result");
                                    if (code.equals("success")) {
                                        //成功的话则更新订单表的状态
                                        userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                                "支付成功",
                                                PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"") + 15,
                                                        param.getFundBillList().indexOf("\"}]"))),
                                                date, param.getOutTradeNo(), id);
                                        //webSocket提醒给用户跳转页面
                                        webSocket.sendTextMessage(id.toString(),"member/power");
                                    } else {
                                        //写入失败的日志(补偿),暂时不会,基本成功.
                                    }
                                }
                               if (param.getTotalAmount().toString().equals("30.00") && param.getSubject().equals("SVIP包月")) {
                                    //调用会员微服务直接更新SVIP包季的天数且完成订单表的更新.
                                    Map<String, Object>  resultMapMemberSvipMonth=payMemberFeign.svipMonth(id);
                                    Object code = resultMapMemberSvipMonth.get("result");
                                    if (code.equals("success")) {
                                        //成功的话则更新订单表的状态
                                        userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                                "支付成功",
                                                PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"") + 15,
                                                        param.getFundBillList().indexOf("\"}]"))),
                                                date, param.getOutTradeNo(), id);
                                        //webSocket提醒给用户跳转页面
                                        webSocket.sendTextMessage(id.toString(),"member/power");
                                    } else {
                                        //写入失败的日志(补偿),暂时不会,基本成功.
                                    }
                                }
                               if (param.getReceiptAmount().toString().equals(userOrder.getTotalAmount().toString()) && param.getSubject().equals("余额充值")){
                                    //成功的话则更新订单表的状态
                                    userOrderMapper.updateResponseByOutTradeNoAndOrderId(param.getReceiptAmount(),
                                            "支付成功",
                                            PayChannel.getUtf(param.getFundBillList().substring(param.getFundBillList().indexOf("\"fundChannel\":\"") + 15,
                                                    param.getFundBillList().indexOf("\"}]"))),
                                            date, param.getOutTradeNo(), id);
                                    //查询wallet主键Id
                                    Long realId=userWalletMapper.selectIdByWalletId(id);
                                    //更新数据库中的余额
                                    userWalletMapper.updateWalletMoneyById(param.getReceiptAmount(),realId);
                                }else {
                                    //写入失败的日志(补偿),暂时不会,基本成功.
                                }
                            } catch (Exception e) {
                                logger.error("支付宝回调业务处理报错,params:" + paramsJson, e);
                            }
                        } else {
                            logger.error("没有处理支付宝回调业务，支付宝交易状态：{},params:{}",trade_status,paramsJson);
                        }
                    }
                });
                // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
                // 业务处理失败，可查看日志进行补偿，跟支付宝已经没多大关系。
                return "success";
            } else {
                logger.info("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
                return "failure";
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", paramsJson, e.getMessage());
            return "failure";
        }
    }

    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }

    //JSON的转换器
    private AlipayNotifyParam buildAlipayNotifyParam(Map<String, String> params) {
        String json = JSON.toJSONString(params);
        return JSON.parseObject(json, AlipayNotifyParam.class);
    }

    /**
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     * 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
     */
    private void check(Map<String, String> params) throws AlipayApiException {
        String outTradeNo = params.get("out_trade_no");

        // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        UserOrder order = userOrderMapper.selectAllByOutTradeNo(outTradeNo);

        if (order == null) {
            throw new AlipayApiException("out_trade_no错误");
        }

        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        BigDecimal total_amount = new BigDecimal(params.get("total_amount")).multiply(new BigDecimal(100));
        if (total_amount.equals(order.getTotalAmount())) {
            throw new AlipayApiException("error total_amount");
        }

        // 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
        // 第三步可根据实际情况省略

        // 4、验证app_id是否为该商户本身。
        if (!params.get("app_id").equals(order.getAppId())) {
            throw new AlipayApiException("app_id不一致");
        }
    }

}
