package com.pay.Service;

import com.github.pagehelper.PageInfo;
import com.pay.Response.CostResponse;
import com.pay.Response.DateRequest;
import com.pay.Response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public interface OrderService {

    //分页获取整个账单
    PageInfo orderPageAll(int page, int count, Long orderId);

    //删除一条账单的记录
    int deleteOrderOne(Long orderId,Long authorId );

    //前端获取个人全部账单的Csv
    List<OrderResponse> orderAll(Long id);

    //获得指定月份的账单Csv
    List<OrderResponse> orderMonthAll(Date monthStart, Long id);

    //获得指定时间段的消费
    CostResponse getCostAll(DateRequest dateRequest, Long id);
}
