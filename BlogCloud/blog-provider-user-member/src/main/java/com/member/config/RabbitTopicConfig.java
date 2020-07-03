package com.member.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Topic路由策略是一种复杂但是灵活的策略，Queue通过routingKey绑定到TopicExchange上，当消息到达TopicExchange后，
 * 它可以根据routingKey将消息路由到一个或者多个Queue上。(与routingKey有关)
 */
@Configuration
public class RabbitTopicConfig {

    public static final String QUEUE_TOPIC_NAME_ONE= "topicMemberOne";
    public static final String QUEUE_TOPIC_NAME_TWO = "topicMemberTwo";
    public static final String QUEUE_TOPIC_NAME_THREE= "topicMemberThree";
    public static final String QUEUE_TOPIC_EXCHANGE_NAME = "whc-topic";

    @Bean
    public Queue queueTopicOne() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_TOPIC_NAME_ONE, durable, exclusive, autoDelete);
    }

    @Bean
    public Queue queueTopicTwo() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_TOPIC_NAME_TWO, durable, exclusive, autoDelete);
    }

    @Bean
    public Queue queueTopicThree() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_TOPIC_NAME_THREE, durable, exclusive, autoDelete);
    }

    @Bean
    TopicExchange topicExchange(){
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new TopicExchange(QUEUE_TOPIC_EXCHANGE_NAME,durable,autoDelete);
    }

    //以.#结束表示开头的信息均到这个Queue上
    @Bean
    Binding topicBindingOne(){
        return BindingBuilder.bind(queueTopicOne()).to(topicExchange())
                .with("topicMemberOne.#");
    }

    //以.#结束表示开头的信息均到这个Queue上
    @Bean
    Binding topicBindingTwo(){
        return BindingBuilder.bind(queueTopicTwo()).to(topicExchange())
                .with("topicMemberTwo.#");
    }

    //以#.+(字段)+.#结束表示有这个字段都传到这个Queue上
    @Bean
    Binding topicBindingThree(){
        return BindingBuilder.bind(queueTopicThree()).to(topicExchange())
                .with("#.topicMemberThree.#");
    }
}
