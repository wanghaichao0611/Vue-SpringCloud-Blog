package com.pay.Service;


import com.alipay.api.AlipayApiException;
import com.pay.Bean.AlipayBean;
import com.pay.Bean.AlipaySvipBean;
import org.springframework.stereotype.Service;

/*支付服务*/
@Service
public interface PayService {

    //支付宝原始支付
    String aliPay(AlipayBean alipayBean) throws AlipayApiException;

    //支付宝二维码支付
    String aliSvipPay(AlipaySvipBean alipaySvipBean) throws AlipayApiException;
}
