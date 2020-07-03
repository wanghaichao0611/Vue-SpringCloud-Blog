package com.pay.Controller;


import com.alipay.api.AlipayApiException;
import com.github.pagehelper.PageInfo;
import com.pay.Bean.AlipayBean;
import com.pay.Bean.AlipaySvipBean;
import com.pay.Entity.UserOrder;
import com.pay.Mapper.UserOrderMapper;
import com.pay.Response.CostResponse;
import com.pay.Response.DateRequest;
import com.pay.Response.OrderResponse;
import com.pay.Service.OrderService;
import com.pay.Service.PayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

//支付宝支付接口
@RestController
public class OrderController {
    @Resource
    private PayService payService;//调用支付服务

    //Crud的Mapper
    @Autowired
    UserOrderMapper userOrderMapper;

    @Autowired
    OrderService orderService;

    //发送信息的模板
    @Autowired
    RabbitTemplate rabbitTemplate;

    //支付宝原始支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/alipayVip")
    public String alipayVip(@RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
                            @RequestParam("total_amount") String total_amount, @RequestParam("body") String body,
                            @RequestParam("id") Long id) throws AlipayApiException {
        //7、商户订单号和ID绑定并且设置时间10分钟提供给异步通知使用(10分钟内完成支付)，Redis实现的延迟队列。
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set(out_trade_no, id.toString());
        jedis.expire(out_trade_no, 600);
        //8、创建订单信息(等待交易状态)
        UserOrder order = new UserOrder();
        order.setOrderId(id);
        order.setAppId("AppId");
        order.setOutTradeNo(out_trade_no);
        order.setSubject(subject);
        order.setTotalAmount(new BigDecimal(total_amount));
        order.setBody(body);
        order.setStoreId("StoreId");
        order.setTradestatus("等待支付");
        order.setCreatetime(new Date());
        //9.获取到返回的订单主键Id
        userOrderMapper.insert(order);
        //10.把订单的主键Id作为生产者发给延迟队列，用RabbitMQ实现的延迟队列。
        rabbitTemplate.convertAndSend("realExchange","realQueueKey",order.getId());
        //11.返回支付结果
        return payService.aliPay(new AlipayBean()
                .setBody(body)
                .setOut_trade_no(out_trade_no)
                .setTotal_amount(new StringBuffer().append(total_amount))
                .setSubject(subject));
    }

    //支付宝二维码支付
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/alipaySvipQrcode")
    public String alipaySvipQrcode(
            @RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
            @RequestParam("total_amount") String total_amount, @RequestParam("store_id") String store_id,
            @RequestParam("body") String body,@RequestParam("id") Long id) throws AlipayApiException {

        //7、商户订单号和ID绑定并且设置时间10分钟提供给异步通知使用(10分钟内完成支付)
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set(out_trade_no, id.toString());
        jedis.expire(out_trade_no, 600);
        //8、创建订单信息(等待交易状态)
        UserOrder order = new UserOrder();
        order.setOrderId(id);
        order.setAppId("AppId");
        order.setOutTradeNo(out_trade_no);
        order.setSubject(subject);
        order.setBody(body);
        order.setTotalAmount(new BigDecimal(total_amount));
        order.setStoreId(store_id);
        order.setTradestatus("等待支付");
        order.setCreatetime(new Date());
        userOrderMapper.insert(order);
        //把订单的主键Id作为生产者发给延迟队列，用RabbitMQ实现的延迟队列。
        rabbitTemplate.convertAndSend("realExchange","realQueueKey",order.getId());
        return payService.aliSvipPay(new AlipaySvipBean()
                .setStore_id(store_id)
                .setOut_trade_no(out_trade_no)
                .setTotal_amount(new StringBuffer().append(total_amount))
                .setSubject(subject));

    }



    //分页获取整个支付账单
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/orderPageAll")
    public Map<String,Object> orderPageAll(@RequestParam(name = "page", defaultValue = "1") int page,
                                           @RequestParam(name = "count", defaultValue = "5") int count,
                                           @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        PageInfo pageInfo=orderService.orderPageAll(page,count,id);
        resultMap.put("success",true);
        resultMap.put("list",pageInfo.getList());
        resultMap.put("total",pageInfo.getTotal());
        return resultMap;
    }

    //前端获取个人全部账单的Csv
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/orderAll")
    public Map<String, Object> orderAll(@RequestParam("id")Long id){
        Map<String, Object> resultMap = new HashMap<>();
        List<OrderResponse> orderResponseList=orderService.orderAll(id);
        resultMap.put("success",true);
        resultMap.put("list",orderResponseList);
        return resultMap;
    }

    //获得指定月份的账单
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/orderMonthAll")
    public Map<String, Object> orderMonthAll(@RequestBody DateRequest request, @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        List<OrderResponse> orderResponseList=orderService.orderMonthAll(request.getMonthStart(),id);
        resultMap.put("success",true);
        resultMap.put("list",orderResponseList);
        return resultMap;
    }

    //获得指定时间段的消费
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/getCost")
    public Map<String, Object> getCost(@RequestBody DateRequest request, @RequestParam("id") Long id){
        Map<String, Object> resultMap = new HashMap<>();
        CostResponse costAll=orderService.getCostAll(request,id);
        if (costAll.getTotalOrder()==0){
            resultMap.put("success",false);
            return resultMap;
        }
        //也可以数据库取出来List，再判断是否存在消费。
        List<CostResponse> costResponseList=new ArrayList<>();
        costResponseList.add(costAll);
        resultMap.put("success",true);
        resultMap.put("list",costResponseList);
        return resultMap;
    }

    //删除一条账单的记录
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/orderDelete")
    public Map<String,Object> orderDelete(@RequestParam("orderId")Long orderId,@RequestParam("authorId")Long authorId){
        Map<String, Object> resultMap = new HashMap<>();
        int result=orderService.deleteOrderOne(orderId,authorId);
        boolean flag=result>0;
        resultMap.put("success",flag);
        resultMap.put("message",flag? "删除成功!":"非法删除!");
        return resultMap;
    }

    //支付宝原始充值余额
    @CrossOrigin(origins = "http://localhost:8088")
    @PostMapping(value = "/rechargeMoney")
    public String rechargeMoney(@RequestParam("out_trade_no") String out_trade_no, @RequestParam("subject") String subject,
                            @RequestParam("total_amount") String total_amount, @RequestParam("body") String body,
                            @RequestParam("id") Long id) throws AlipayApiException {
        //7、商户订单号和ID绑定并且设置时间10分钟提供给异步通知使用(10分钟内完成支付)，Redis实现的延迟队列。
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set(out_trade_no, id.toString());
        jedis.expire(out_trade_no, 600);
        //8、创建订单信息(等待交易状态)
        UserOrder order = new UserOrder();
        order.setOrderId(id);
        order.setAppId("AppId");
        order.setOutTradeNo(out_trade_no);
        order.setSubject(subject);
        order.setTotalAmount(new BigDecimal(total_amount));
        order.setBody(body);
        order.setStoreId("StoreId");
        order.setTradestatus("等待支付");
        order.setCreatetime(new Date());
        //9.获取到返回的订单主键Id
        userOrderMapper.insert(order);
        //10.把订单的主键Id作为生产者发给延迟队列，用RabbitMQ实现的延迟队列。
        rabbitTemplate.convertAndSend("realExchangeNoCoupon","realQueueKeyNoCoupon",order.getId());
        //11.返回支付结果
        return payService.aliPay(new AlipayBean()
                .setBody(body)
                .setOut_trade_no(out_trade_no)
                .setTotal_amount(new StringBuffer().append(total_amount))
                .setSubject(subject));
    }

}
