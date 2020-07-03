package com.pay.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Repository
public class OrderResponse {
    private Long id;

    private String outTradeNo;

    private BigDecimal totalAmount;

    private BigDecimal receiptAmount;

    private String subject;

    private String body;

    private String fundchannel;

    private String tradestatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endtime;
}
