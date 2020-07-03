package com.article.Config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


//配置RabbitMQ延迟队列(文章Id的延迟队列)
@Configuration
public class ArticleRabbitConfig {

    //创建延迟队列
    @Bean
    public Queue articleDeadQueue(){
        //创建延迟队列Map
        Map<String,Object> args=new HashMap<>();
        //创建延迟交换机
        args.put("x-dead-letter-exchange","articleDeadExchange");
        //创建延迟路由
        args.put("x-dead-letter-routing-key","articleDeadQueueKey");
        //设定TTL时间，真实的文章延迟队列应该为1天86400000毫秒（多少天都可以，因为消息最大的TTL为一天）,测试30S
        args.put("x-message-ttl",86400000);
        //返回延迟队列实例
        return new Queue("articleDeadQueue",true,false,false,args);
    }

    //创建面向生产者的基本类型Topic交换机
    @Bean
    public TopicExchange  articleBasicExchange(){
        //创建并且返回实例
        return new TopicExchange("articleRealExchange",true,false);
    }


    //创建面向生产者的绑定关系
    @Bean
    public Binding articleRealBinding(){
        //创建返回绑定的实例
        return BindingBuilder.bind(articleDeadQueue()).to(articleBasicExchange())
                .with("articleRealQueueKey");
    }

    //创建面向消费者的队列
    @Bean
    public Queue articleRealQueue(){
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue("articleRealQueue", durable, exclusive, autoDelete);
    }

    //创建延迟队列交换机
    @Bean
    public TopicExchange articleDeadExchange(){
        return new TopicExchange("articleDeadExchange",true,false);
    }

    //创建延迟队列路由及其绑定
    @Bean
    public Binding articleDeadBinding(){
        //创建延迟路由及其绑定实例
        return BindingBuilder.bind(articleRealQueue()).to(articleDeadExchange())
                .with("articleDeadQueueKey");
    }
}
