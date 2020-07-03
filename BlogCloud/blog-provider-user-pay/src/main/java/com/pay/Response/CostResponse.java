package com.pay.Response;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class CostResponse {
    private String totalPayPercent;
    private int totalPay;
    private int totalPayMoney;
    private int totalOrder;
    private int totalOrderMoney;
}
