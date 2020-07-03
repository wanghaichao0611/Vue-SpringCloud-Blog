package com.article.Config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


//审核文章的延迟队列
@Configuration
public class ExamineRabbitConfig {

    //创建延迟队列
    @Bean
    public Queue examineDeadQueue(){
        //创建延迟队列Map
        Map<String,Object> args=new HashMap<>();
        //创建延迟交换机
        args.put("x-dead-letter-exchange","examineDeadExchange");
        //创建延迟路由
        args.put("x-dead-letter-routing-key","examineDeadQueueKey");
        //设定TTL时间，真实审核时间为3-5分钟，30s是测试用的
        args.put("x-message-ttl",30000);
        //返回延迟队列实例
        return new Queue("examineDeadQueue",true,false,false,args);
    }

    //创建面向生产者的基本类型Topic交换机
    @Bean
    public TopicExchange examineBasicExchange(){
        //创建并且返回实例
        return new TopicExchange("examineRealExchange",true,false);
    }


    //创建面向生产者的绑定关系
    @Bean
    public Binding examineRealBinding(){
        //创建返回绑定的实例
        return BindingBuilder.bind(examineDeadQueue()).to(examineBasicExchange())
                .with("examineRealQueueKey");
    }

    //创建面向消费者的队列
    @Bean
    public Queue examineRealQueue(){
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue("examineRealQueue", durable, exclusive, autoDelete);
    }

    //创建延迟队列交换机
    @Bean
    public TopicExchange examineDeadExchange(){
        return new TopicExchange("examineDeadExchange",true,false);
    }

    //创建延迟队列路由及其绑定
    @Bean
    public Binding examineDeadBinding(){
        //创建延迟路由及其绑定实例
        return BindingBuilder.bind(examineRealQueue()).to(examineDeadExchange())
                .with("examineDeadQueueKey");
    }
}
