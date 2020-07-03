package com.pay.ServiceImpl;

import com.alipay.api.AlipayApiException;
import com.pay.Bean.AlipayBean;
import com.pay.Bean.AlipaySvipBean;
import com.pay.Config.AlipayUtil;
import com.pay.Service.PayService;
import org.springframework.stereotype.Service;


/* 支付服务 */
@Service(value = "alipayOrderService")
public class PayServiceImpl implements PayService {


    //支付宝原始支付
    @Override
    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {
        AlipayUtil alipayUtil=new AlipayUtil();
        return alipayUtil.connect(alipayBean);
    }

    //支付宝二维码支付
    @Override
    public String aliSvipPay(AlipaySvipBean alipaySvipBean) throws AlipayApiException {
        AlipayUtil alipayUtil=new AlipayUtil();
        return alipayUtil.svipConnect(alipaySvipBean);
    }
}
