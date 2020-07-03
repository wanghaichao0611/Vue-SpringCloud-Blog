package com.requset;


import lombok.Data;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Data
@Repository
public class PersonSvipRequest {
    String out_trade_no;
    String subject;
    String body;
    String walletPassword;
    BigDecimal balanceSvip;
    Integer balanceSvipNumber;
}
