package com.pay.Mapper;

import com.pay.Entity.UserOrder;
import com.pay.Response.CostResponse;
import com.pay.Response.OrderResponse;import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserOrderMapper {

    //返回订单的主键Id
    int insert(UserOrder record);

    //根据商户订单号查询全部
    UserOrder selectAllByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    //更新订单数据(下单标志)
    int updateResponseByOutTradeNoAndOrderId(@Param("updatedReceiptAmount") BigDecimal updatedReceiptAmount, @Param("updatedTradestatus") String updatedTradestatus, @Param("channel") String channel, @Param("date") Date date, @Param("outTradeNo") String outTradeNo, @Param("orderId") Long orderId);

    //分页查询账单
    List<OrderResponse> findUserOrderAllById(@Param("orderId") Long orderId);

    //根据账单Id查询作者Id
    Long selectOrderIdById(@Param("id") Long id);

    //根据作者Id与文章Id删除记录
    int deleteById(@Param("id") Long id);

    //查询指定月份的账单(根据时间)
    List<OrderResponse> findUserOrderAllByIdAndTime(@Param("orderId") Long orderId,@Param("from") Date form,@Param("to") Date to);

    //查询指定时间段的消费
    CostResponse findCostAll(@Param("orderId") Long orderId, @Param("from") Date form, @Param("to") Date to);

    //根据主键查询是否存在实付金额
    BigDecimal selectReceiptAmountById(@Param("id")Long id);

    //更新交易结束时间以及未收到钱款
    int updateReceiptAmountAndEndtimeById(@Param("updatedEndtime")Date updatedEndtime,@Param("id")Long id);

    //根据主键Id查询所有的信息
    UserOrder selectAllById(@Param("id")Long id);


}