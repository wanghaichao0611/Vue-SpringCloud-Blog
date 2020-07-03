package com.member.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


//超级会员到期通知的队列
@Configuration
public class RabbitSvipTimeConfig {

    //创建延迟队列
    @Bean
    public Queue svipTimeDeadQueue(){
        //创建延迟队列Map
        Map<String,Object> args=new HashMap<>();
        //创建延迟交换机
        args.put("x-dead-letter-exchange","svipTimeDeadExchange");
        //创建延迟路由
        args.put("x-dead-letter-routing-key","svipTimeDeadQueueKey");
        //设定TTL时间，真实的时间到期延迟队列应该为10年
        args.put("x-message-ttl",315360000000L);
        //返回延迟队列实例
        return new Queue("svipTimeDeadQueue",true,false,false,args);
    }

    //创建面向生产者的基本类型Topic交换机
    @Bean
    public TopicExchange svipTimeBasicExchange(){
        //创建并且返回实例
        return new TopicExchange("svipTimeRealExchange",true,false);
    }


    //创建面向生产者的绑定关系
    @Bean
    public Binding svipTimeRealBinding(){
        //创建返回绑定的实例
        return BindingBuilder.bind(svipTimeDeadQueue()).to(svipTimeBasicExchange())
                .with("svipTimeRealQueueKey");
    }

    //创建面向消费者的队列
    @Bean
    public Queue svipTimeRealQueue(){
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue("svipTimeRealQueue", durable, exclusive, autoDelete);
    }

    //创建延迟队列交换机
    @Bean
    public TopicExchange svipTimeDeadExchange(){
        return new TopicExchange("svipTimeDeadExchange",true,false);
    }

    //创建延迟队列路由及其绑定
    @Bean
    public Binding svipTimeDeadBinding(){
        //创建延迟路由及其绑定实例
        return BindingBuilder.bind(svipTimeRealQueue()).to(svipTimeDeadExchange())
                .with("svipTimeDeadQueueKey");
    }
}
