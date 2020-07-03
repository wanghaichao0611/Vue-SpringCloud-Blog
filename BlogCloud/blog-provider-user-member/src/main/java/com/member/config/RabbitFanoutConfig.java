package com.member.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * FanoutExchange的数据交换策略是把所有到达FanoutExchange的消息转发给所有与它绑定的Queue，在这种策略下，
 * routingKey将不起任何作用。(与routingKey无关)
 */
@Configuration
public class RabbitFanoutConfig {

    public static final String QUEUE_FANOUT_NAME_ONE= "fanoutMemberOne";
    public static final String QUEUE_FANOUT_NAME_TWO = "fanoutMemberTwo";
    public static final String QUEUE_FANOUT_EXCHANGE_NAME = "whc-fanout";

    @Bean
    public Queue queueFanoutOne() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_FANOUT_NAME_ONE, durable, exclusive, autoDelete);
    }

    @Bean
    public Queue queueFanoutTwo() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_FANOUT_NAME_TWO, durable, exclusive, autoDelete);
    }

    @Bean
    FanoutExchange fanoutExchange(){
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new FanoutExchange(QUEUE_FANOUT_EXCHANGE_NAME,durable,autoDelete);
    }

    @Bean
    Binding fanoutBindingOne(){
        return BindingBuilder.bind(queueFanoutOne()).to(fanoutExchange());
    }

    @Bean
    Binding fanoutBindingTwo(){
        return BindingBuilder.bind(queueFanoutTwo()).to(fanoutExchange());
    }
}
