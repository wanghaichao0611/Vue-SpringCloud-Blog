package com.member.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * DirectExchange的路由策略是将消息队列绑定到一个DirectExchange上，可以指定routingKey。(与routingKey有关)
 */
@Configuration
public class RabbitDirectConfig {
    public static final String QUEUE_DIRECT_NAME = "directMemberOne";
    public static final String QUEUE_DIRECT_EXCHANGE_NAME = "whc-direct";

    @Bean
    public Queue queue() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_DIRECT_NAME, durable, exclusive, autoDelete);
    }

    @Bean
    public DirectExchange exchange() {
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new DirectExchange(QUEUE_DIRECT_EXCHANGE_NAME, durable, autoDelete);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_DIRECT_NAME);
    }
}

