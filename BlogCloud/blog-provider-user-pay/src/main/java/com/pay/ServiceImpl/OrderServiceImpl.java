package com.pay.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pay.Mapper.UserOrderMapper;
import com.pay.Response.CostResponse;
import com.pay.Response.DateRequest;
import com.pay.Response.OrderResponse;
import com.pay.Service.OrderService;
import com.pay.Util.UtilDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    @Autowired
    UserOrderMapper userOrderMapper;

    //控制台
    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    //分页获取整个账单
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public PageInfo orderPageAll(int page, int count, Long orderId) {
        PageHelper.startPage(page,count);
        List<OrderResponse> orderResponseList=userOrderMapper.findUserOrderAllById(orderId);
        PageInfo pageInfo=new PageInfo(orderResponseList);
        return pageInfo;
    }

    //删除一条账单的记录
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int deleteOrderOne(Long orderId, Long authorId) {
        //查询要删除的账单Id的作者Id是否一致
        Long realId=userOrderMapper.selectOrderIdById(orderId);
        if (realId.equals(authorId)){
            //一致则根据两个Id删除账单
            userOrderMapper.deleteById(orderId);
            return SUCCESS;
        }else {
            return FAILED;
        }
    }

    //前端获取个人全部账单的Csv
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<OrderResponse> orderAll(Long id) {
        List<OrderResponse> orderResponseList=userOrderMapper.findUserOrderAllById(id);
        return orderResponseList;
    }

    //获得指定月份的账单Csv
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public List<OrderResponse> orderMonthAll(Date monthStart, Long id) {
        List<OrderResponse> orderResponseList=userOrderMapper.findUserOrderAllByIdAndTime(id,monthStart,
                UtilDate.toDate(UtilDate.getMonthEnd(UtilDate.toDateString(monthStart.toString()))));
        LOGGER.info(new Date(monthStart.toString())+""+UtilDate.toDate(UtilDate.getMonthEnd(UtilDate.toDateString(monthStart.toString()))));
        return orderResponseList;
    }

    //获得指定时间段的消费
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public CostResponse getCostAll(DateRequest dateRequest, Long id) {
        CostResponse costAll=userOrderMapper.findCostAll(id,dateRequest.getCostStart(),dateRequest.getCostEnd());
        LOGGER.info(dateRequest+"");
        return costAll;
    }
}
