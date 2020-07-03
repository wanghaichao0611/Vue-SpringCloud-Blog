package com.pay.Bean;

import lombok.Data;
import lombok.experimental.Accessors;

//支付实体对象(原始支付方式)
@Data
@Accessors(chain = true)
public class AlipayBean {

    //商户订单号，必填
    private String out_trade_no;

    //订单名称，必填
    private String subject;

    //付款金额，必填
    private StringBuffer total_amount;

    //商品描述，可空
    private String body;

    //超时时间参数
    private String timeout_express="10m";
    private String product_code="FAST_INSTANT_TRADE_PAY";

}
