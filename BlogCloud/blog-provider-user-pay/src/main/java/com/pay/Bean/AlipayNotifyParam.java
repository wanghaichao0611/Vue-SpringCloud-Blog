package com.pay.Bean;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Repository
public class AlipayNotifyParam implements Serializable {

    private String appId;
    private String tradeNo; // 支付宝交易凭证号
    private String outTradeNo; // 原支付请求的商户订单号
    private String outBizNo; // 商户业务ID，主要是退款通知中返回退款申请的流水号
    private String buyerId; // 买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
    private String buyerLogonId; // 买家支付宝账号
    private String sellerId; // 卖家支付宝用户号
    private String sellerEmail; // 卖家支付宝账号
    private String tradeStatus; // 交易目前所处的状态，见交易状态说明
    private BigDecimal totalAmount; // 本次交易支付的订单金额
    private BigDecimal receiptAmount; // 商家在交易中实际收到的款项
    private BigDecimal buyerPayAmount; // 用户在交易中支付的金额
    private BigDecimal refundFee; // 退款通知中，返回总退款金额，单位为元，支持两位小数
    private String subject; // 商品的标题/交易标题/订单标题/订单关键字等
    private String body; // 该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来
    private Date gmtCreate; // 该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss
    private Date gmtPayment; // 该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss
    private Date gmtRefund; // 该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S
    private Date gmtClose; // 该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss
    private String fundBillList; // 支付成功的各个渠道金额信息,array
    private String passbackParams; // 公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。

}

