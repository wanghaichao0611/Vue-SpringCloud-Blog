package com.pay.Request;


import lombok.Data;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Data
@Repository
public class PersonVipRequest {
    String out_trade_no;
    String subject;
    String body;
    String walletPassword;
    BigDecimal balanceVip;
    Integer balanceVipNumber;
}
