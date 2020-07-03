package com.pay.Config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitNoCouponConfig {

    //创建延迟队列(用户个人充值不需要优惠，30分钟交易时间)
    @Bean
    public Queue deadQueueNoCoupon(){
        //创建延迟队列Map
        Map<String,Object> args=new HashMap<>();
        //创建延迟交换机
        args.put("x-dead-letter-exchange","deadExchangeNoCoupon");
        //创建延迟路由
        args.put("x-dead-letter-routing-key","deadQueueKeyNoCoupon");
        //设定TTL时间，50S先做测试，真实延迟应该为30分钟
        args.put("x-message-ttl",50000);
        //返回延迟队列实例
        return new Queue("deadQueueNoCoupon",true,false,false,args);
    }

    //创建面向生产者的基本类型Topic交换机
    @Bean
    public TopicExchange basicExchangeNoCoupon(){
        //创建并且返回实例
        return new TopicExchange("realExchangeNoCoupon",true,false);
    }


    //创建面向生产者的绑定关系
    @Bean
    public Binding realBindingNoCoupon(){
        //创建返回绑定的实例
        return BindingBuilder.bind(deadQueueNoCoupon()).to(basicExchangeNoCoupon())
                .with("realQueueKeyNoCoupon");
    }

    //创建面向消费者的队列
    @Bean
    public Queue realQueueNoCoupon(){
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue("realQueueNoCoupon", durable, exclusive, autoDelete);
    }

    //创建延迟队列交换机
    @Bean
    public TopicExchange deadExchangeNoCoupon(){
        return new TopicExchange("deadExchangeNoCoupon",true,false);
    }

    //创建延迟队列路由及其绑定
    @Bean
    public Binding deadBindingNoCoupon(){
        //创建延迟路由及其绑定实例
        return BindingBuilder.bind(realQueueNoCoupon()).to(deadExchangeNoCoupon())
                .with("deadQueueKeyNoCoupon");
    }
}
