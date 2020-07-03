package com.pay.Bean;


import lombok.Data;
import org.springframework.stereotype.Component;


//POJO配置
@Data
@Component
public class AlipayConfig {

    // 商户ID
    private String appid = "";
    // 私钥
    private String rsa_private_key = "";
    // 异步回调地址
    private String notify_url="http://内网穿透地址/alipayCallback";
    // 同步回调地址
    private String return_url="http://localhost:8088/PaySuccess";
    // 请求网关地址
    private String gateway_url="https://openapi.alipaydev.com/gateway.do";
    // 编码
    private String charset = "utf-8";
    // 返回格式
    private String format = "json";
    // 支付宝公钥
    private String alipay_public_key = "";
    // RSA2
    private String signtype = "RSA2";
}
