package com.member.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Header路由策略是一种使用较少的路由策略，根据消息的Header将消息路由到不同的Queue上。(与routingKey无关)
 */
@Configuration
public class RabbitHeaderConfig {

    public static final String QUEUE_Header_NAME_ONE= "headerMemberOne";
    public static final String QUEUE_Header_NAME_TWO = "headerMemberTwo";
    public static final String QUEUE_Header_EXCHANGE_NAME = "whc-header";

    @Bean
    public Queue queueHeaderOne() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_Header_NAME_ONE, durable, exclusive, autoDelete);
    }

    @Bean
    public Queue queueHeaderTwo() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_Header_NAME_TWO, durable, exclusive, autoDelete);
    }

    @Bean
    HeadersExchange headersExchange(){
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new HeadersExchange(QUEUE_Header_EXCHANGE_NAME,durable,autoDelete);
    }

    //匹配key/value均符合的消息到这个Queue上
    @Bean
    Binding headerBindingOne(){
        Map<String,Object> map=new HashMap<>();
        map.put("name","whc");
        return BindingBuilder.bind(queueHeaderOne()).to(headersExchange())
                .whereAny(map).match();
    }


    //只要含有"字段"key均，无论值多少都要路由到这个Queue上
    @Bean
    Binding headerBingTwo(){
        return BindingBuilder.bind(queueHeaderTwo()).to(headersExchange())
                .where("all").exists();
    }
}
