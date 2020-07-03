package com.pay.Bean;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AlipaySvipBean {

    private String out_trade_no;

    private StringBuffer total_amount;

    private String subject;

    private String store_id;

    //超时时间参数
    private String timeout_express="10m";
}
