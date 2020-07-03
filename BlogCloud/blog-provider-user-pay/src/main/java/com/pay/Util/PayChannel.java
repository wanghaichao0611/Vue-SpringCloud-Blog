package com.pay.Util;


public class PayChannel {

    public static String getUtf(String pay){
        String utf="";
        switch (pay){
            case "COUPON":
                utf="支付宝红包";
                break;
            case "ALIPAYACCOUNT":
                utf="支付宝余额";
                break;
            case "POINT":
                utf="集分宝";
                break;
            case "DISCOUNT":
                utf="折扣卷";
                break;
            case "PCARD":
                utf="预付卡";
                break;
            case "FINANCEACCOUNT":
                utf="余额宝";
                break;
            case "MCARD":
                utf="商家储值卡";
                break;
            case "MDISCOUNT":
                utf="商户优惠券";
                break;
            case "MCOUPON":
                utf="商户红包";
                break;
            case "PCREDIT":
                utf="蚂蚁花呗";
                break;
        }
        return utf;
    }
}
